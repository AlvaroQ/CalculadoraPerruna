package com.alvaroquintana.edadperruna.managers

import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {
    suspend fun shouldShowAd(): Boolean {
        val count = preferencesRepository.screenViewerCount.first()
        preferencesRepository.incrementScreenViewer()
        return count != 0 && count % 4 == 0
    }
}
