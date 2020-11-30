package com.alvaroquintana.edadperruna.data.database

import com.alvaroquintana.data.source.LocalDataSource
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.data.toDomainDog
import com.alvaroquintana.edadperruna.data.toRoomDog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: DogDatabase) : LocalDataSource {

    private val dogDao = db.dogDao()

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) { dogDao.dogCount() <= 0 }

    override suspend fun saveDogs(dogs: List<Dog>) {
        withContext(Dispatchers.IO) { dogDao.insertDogs(dogs.map { it.toRoomDog() }) }
    }

    override suspend fun getDogs(): MutableList<Dog> = withContext(Dispatchers.IO) {
        dogDao.getAll().map { it.toDomainDog() } as MutableList<Dog>
    }
}