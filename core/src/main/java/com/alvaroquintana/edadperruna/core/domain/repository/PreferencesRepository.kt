package com.alvaroquintana.edadperruna.core.domain.repository

import com.alvaroquintana.edadperruna.core.domain.model.FavoriteBreed
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val screenViewerCount: Flow<Int>
    val themeMode: Flow<String>
    val favoriteBreed: Flow<FavoriteBreed?>

    suspend fun incrementScreenViewer()
    suspend fun setThemeMode(mode: String)
    suspend fun setFavoriteBreed(breed: FavoriteBreed)
    suspend fun clearFavoriteBreed()
}
