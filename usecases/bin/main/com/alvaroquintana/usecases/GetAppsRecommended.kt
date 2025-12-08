package com.alvaroquintana.usecases

import com.alvaroquintana.data.repository.AppsRecommendedRepository
import com.alvaroquintana.domain.App

class GetAppsRecommended(private val appsRecommendedRepository: AppsRecommendedRepository) {

    suspend operator fun invoke(): MutableList<App> = appsRecommendedRepository.getAppsRecommended()

}
