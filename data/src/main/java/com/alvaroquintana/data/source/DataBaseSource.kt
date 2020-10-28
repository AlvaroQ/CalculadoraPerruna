package com.alvaroquintana.data.source

import com.alvaroquintana.domain.App
import com.alvaroquintana.domain.Dog

interface DataBaseSource {
    suspend fun getBreedList(): MutableList<Dog>
    suspend fun getAppsRecommended(): MutableList<App>
}