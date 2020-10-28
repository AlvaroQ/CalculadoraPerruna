package com.alvaroquintana.data.source

import com.alvaroquintana.domain.Dog

interface DataBaseSource {
    suspend fun getBreedList(): MutableList<Dog>
}