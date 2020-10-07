package com.alvaroquintana.data.datasource

import com.alvaroquintana.domain.Dog

interface DataBaseSource {
    suspend fun getBreedList(): MutableList<Dog>
}