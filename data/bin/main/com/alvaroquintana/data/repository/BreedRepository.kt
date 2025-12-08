package com.alvaroquintana.data.repository

import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.domain.Dog

class BreedRepository(
    private val dataBaseSource: DataBaseSource) {

    suspend fun getBreedList(): MutableList<Dog> {
        return dataBaseSource.getBreedList()
    }

    suspend fun getBreedDescription(breedId: String): Dog? {
        return dataBaseSource.getBreedDescription(breedId)
    }
}