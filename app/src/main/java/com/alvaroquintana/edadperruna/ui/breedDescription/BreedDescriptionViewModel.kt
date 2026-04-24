package com.alvaroquintana.edadperruna.ui.breedDescription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaroquintana.edadperruna.core.data.network.ConnectivityObserver
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.model.FavoriteBreed
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import com.alvaroquintana.edadperruna.managers.AdManager
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.Screens
import com.alvaroquintana.edadperruna.managers.screenViewed
import com.alvaroquintana.edadperruna.ui.common.UiState
import com.alvaroquintana.edadperruna.wearsync.FavoriteBreedPayload
import com.alvaroquintana.edadperruna.wearsync.WearSyncPublisher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDescriptionViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val preferencesRepository: PreferencesRepository,
    private val wearSyncPublisher: WearSyncPublisher,
    private val adManager: AdManager,
    private val connectivityObserver: ConnectivityObserver,
    private val analytics: Analytics,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Dog?>>(UiState.Loading)
    val uiState: StateFlow<UiState<Dog?>> = _uiState.asStateFlow()

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private val _showNoInternet = MutableStateFlow(false)
    val showNoInternet: StateFlow<Boolean> = _showNoInternet.asStateFlow()

    val favoriteBreed: StateFlow<FavoriteBreed?> = preferencesRepository.favoriteBreed
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    init {
        analytics.screenViewed(Screens.BREED_DESCRIPTION)
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

    /**
     * Persists the breed as the user's favorite and pushes it to the watch (no-op if
     * no Wear OS device is paired). Toggling the same breed clears the favorite.
     */
    fun toggleFavorite(breedId: String, name: String, imageUrl: String, avgLifeYears: Int) {
        viewModelScope.launch {
            // Read from the source (not the StateFlow cache) — toggle should work even
            // before the UI starts collecting `favoriteBreed`.
            val current = preferencesRepository.favoriteBreed.first()
            if (current?.breedId == breedId) {
                preferencesRepository.clearFavoriteBreed()
                return@launch
            }
            val breed = FavoriteBreed(
                breedId = breedId,
                name = name,
                imageUrl = imageUrl,
                avgLifeYears = avgLifeYears.coerceAtLeast(MIN_AVG_LIFE),
            )
            preferencesRepository.setFavoriteBreed(breed)
            wearSyncPublisher.publishFavoriteBreed(
                FavoriteBreedPayload(
                    name = breed.name,
                    imageUrl = breed.imageUrl,
                    avgLifeYears = breed.avgLifeYears,
                ),
            )
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
        const val MIN_AVG_LIFE = 1
    }
}
