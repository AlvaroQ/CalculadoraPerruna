package com.alvaroquintana.data.repository

import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.data.source.LocalDataSource
import com.alvaroquintana.domain.Dog

class BreedListRepository(
    private val localDataSource: LocalDataSource,
    private val dataBaseSource: DataBaseSource) {

    suspend fun getBreedList(): MutableList<Dog> {

        if (localDataSource.isEmpty()) {
            val dogs = dataBaseSource.getBreedList()
            localDataSource.saveDogs(dogs)
        }

        return localDataSource.getDogs()
    }

}