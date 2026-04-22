package com.alvaroquintana.edadperruna.core.data.repository

import com.alvaroquintana.edadperruna.core.data.local.dao.DogDao
import com.alvaroquintana.edadperruna.core.data.local.entity.toDomain
import com.alvaroquintana.edadperruna.core.data.local.entity.toEntity
import com.alvaroquintana.edadperruna.core.data.remote.FirestoreBreedDataSource
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreBreedDataSource,
    private val dogDao: DogDao,
) : BreedRepository {

    override fun getBreedList(): Flow<List<Dog>> = dogDao.getAllDogs()
        .map { entities -> entities.map { it.toDomain() } }
        .onStart {
            if (dogDao.count() == 0) {
                refreshBreeds()
            }
        }

    override fun getBreedDescription(breedId: String): Flow<Dog?> = dogDao.getDogById(breedId)
        .map { entity ->
            entity?.toDomain() ?: firestoreDataSource.getBreedDescription(breedId)
        }

    override suspend fun refreshBreeds() {
        val remoteDogs = firestoreDataSource.getBreedList()
        if (remoteDogs.isNotEmpty()) {
            dogDao.deleteAll()
            dogDao.insertAll(remoteDogs.map { it.toEntity() })
        }
    }
}
