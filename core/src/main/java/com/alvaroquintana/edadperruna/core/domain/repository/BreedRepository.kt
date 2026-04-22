package com.alvaroquintana.edadperruna.core.domain.repository

import com.alvaroquintana.edadperruna.core.domain.model.Dog
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    fun getBreedList(): Flow<List<Dog>>
    fun getBreedDescription(breedId: String): Flow<Dog?>
    suspend fun refreshBreeds()
}
