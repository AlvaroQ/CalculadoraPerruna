package com.alvaroquintana.edadperruna.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val screenViewerCount: Flow<Int>
    val themeMode: Flow<String>
    suspend fun incrementScreenViewer()
    suspend fun setThemeMode(mode: String)
}
