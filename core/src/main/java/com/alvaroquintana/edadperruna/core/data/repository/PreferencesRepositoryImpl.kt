package com.alvaroquintana.edadperruna.core.data.repository

import com.alvaroquintana.edadperruna.core.data.local.datastore.PreferencesDataSource
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : PreferencesRepository {

    override val screenViewerCount: Flow<Int> = preferencesDataSource.screenViewerCount
    override val themeMode: Flow<String> = preferencesDataSource.themeMode

    override suspend fun incrementScreenViewer() = preferencesDataSource.incrementScreenViewer()
    override suspend fun setThemeMode(mode: String) = preferencesDataSource.setThemeMode(mode)
}
