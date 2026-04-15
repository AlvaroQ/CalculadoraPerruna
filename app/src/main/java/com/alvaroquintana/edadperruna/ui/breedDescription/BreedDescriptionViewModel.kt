package com.alvaroquintana.edadperruna.ui.breedDescription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaroquintana.edadperruna.core.data.network.ConnectivityObserver
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import com.alvaroquintana.edadperruna.managers.AdManager
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDescriptionViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val adManager: AdManager,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Dog?>>(UiState.Loading)
    val uiState: StateFlow<UiState<Dog?>> = _uiState.asStateFlow()

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private val _showNoInternet = MutableStateFlow(false)
    val showNoInternet: StateFlow<Boolean> = _showNoInternet.asStateFlow()

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_DESCRIPTION)
    }

    fun loadBreed(breedId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (!connectivityObserver.isOnline) {
                _showNoInternet.value = true
            }
            _showAd.value = adManager.shouldShowAd()
            breedRepository.getBreedDescription(breedId)
                .catch { _uiState.value = UiState.Error(it.message ?: "") }
                .collect { dog ->
                    _uiState.value = UiState.Success(dog)
                }
        }
    }

    fun dismissNoInternet() {
        _showNoInternet.value = false
    }
}
