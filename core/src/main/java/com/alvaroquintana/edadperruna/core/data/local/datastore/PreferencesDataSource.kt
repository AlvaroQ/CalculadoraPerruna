package com.alvaroquintana.edadperruna.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

    private companion object {
        val SCREEN_VIEWER_COUNT = intPreferencesKey("screen_viewer_count")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }
}
