package com.alvaroquintana.usecases

import com.alvaroquintana.domain.App
import com.alvaroquintana.data.repository.AppsRecommendedRepository

class GetAppsRecommended(private val appsRecommendedRepository: AppsRecommendedRepository) {

    suspend fun invoke(): MutableList<App> = appsRecommendedRepository.getAppsRecommended()

}
