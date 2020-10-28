package com.alvaroquintana.data.repository

import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.domain.App

class AppsRecommendedRepository(private val dataBaseSource: DataBaseSource) {

    suspend fun getAppsRecommended(): MutableList<App> = dataBaseSource.getAppsRecommended()

}