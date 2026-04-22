package com.alvaroquintana.edadperruna.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.Screens
import com.alvaroquintana.edadperruna.managers.screenViewed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val analytics: Analytics,
) : ViewModel() {

    val themeMode: StateFlow<String> = preferencesRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    init {
        analytics.screenViewed(Screens.SETTINGS)
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }
}
