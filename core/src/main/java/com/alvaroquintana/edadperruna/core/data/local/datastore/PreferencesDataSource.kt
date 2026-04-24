package com.alvaroquintana.edadperruna.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alvaroquintana.edadperruna.core.domain.model.FavoriteBreed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val screenViewerCount: Flow<Int> = dataStore.data.map { prefs ->
        prefs[SCREEN_VIEWER_COUNT] ?: 0
    }

    val themeMode: Flow<String> = dataStore.data.map { prefs ->
        prefs[THEME_MODE] ?: "system"
    }

    val favoriteBreed: Flow<FavoriteBreed?> = dataStore.data.map { prefs ->
        val id = prefs[FAVORITE_BREED_ID] ?: return@map null
        FavoriteBreed(
            breedId = id,
            name = prefs[FAVORITE_BREED_NAME].orEmpty(),
            imageUrl = prefs[FAVORITE_BREED_IMAGE].orEmpty(),
            avgLifeYears = prefs[FAVORITE_BREED_LIFE] ?: DEFAULT_AVG_LIFE,
        )
    }

    suspend fun incrementScreenViewer() {
        dataStore.edit { prefs ->
            val current = prefs[SCREEN_VIEWER_COUNT] ?: 0
            prefs[SCREEN_VIEWER_COUNT] = current + 1
        }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[THEME_MODE] = mode
        }
    }

    suspend fun setFavoriteBreed(breed: FavoriteBreed) {
        dataStore.edit { prefs ->
            prefs[FAVORITE_BREED_ID] = breed.breedId
            prefs[FAVORITE_BREED_NAME] = breed.name
            prefs[FAVORITE_BREED_IMAGE] = breed.imageUrl
            prefs[FAVORITE_BREED_LIFE] = breed.avgLifeYears
        }
    }

    suspend fun clearFavoriteBreed() {
        dataStore.edit { prefs ->
            prefs.remove(FAVORITE_BREED_ID)
            prefs.remove(FAVORITE_BREED_NAME)
            prefs.remove(FAVORITE_BREED_IMAGE)
            prefs.remove(FAVORITE_BREED_LIFE)
        }
    }

    private companion object {
        val SCREEN_VIEWER_COUNT = intPreferencesKey("screen_viewer_count")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val FAVORITE_BREED_ID = stringPreferencesKey("favorite_breed_id")
        val FAVORITE_BREED_NAME = stringPreferencesKey("favorite_breed_name")
        val FAVORITE_BREED_IMAGE = stringPreferencesKey("favorite_breed_image")
        val FAVORITE_BREED_LIFE = intPreferencesKey("favorite_breed_avg_life_years")
        const val DEFAULT_AVG_LIFE = 12
    }
}
