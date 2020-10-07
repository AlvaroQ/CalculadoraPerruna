package com.alvaroquintana.data.repository

import com.alvaroquintana.data.datasource.DataBaseSource
import com.alvaroquintana.domain.Dog

class BreedListRepository(private val dataBaseSource: DataBaseSource) {

    suspend fun getBreedList(): MutableList<Dog> = dataBaseSource.getBreedList()

}