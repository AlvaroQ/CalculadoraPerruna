package com.alvaroquintana.edadperruna.ui.breedList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaroquintana.edadperruna.core.data.network.ConnectivityObserver
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import com.alvaroquintana.edadperruna.managers.AdManager
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val adManager: AdManager,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Dog>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Dog>>> = _uiState.asStateFlow()

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private val _showNoInternet = MutableStateFlow(false)
    val showNoInternet: StateFlow<Boolean> = _showNoInternet.asStateFlow()

    private val _events = Channel<BreedListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_BREED_LIST)
        loadBreeds()
    }

    fun loadBreeds() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (!connectivityObserver.isOnline) {
                _showNoInternet.value = true
            }
            breedRepository.getBreedList()
                .catch { _uiState.value = UiState.Error(it.message ?: "") }
                .collect { dogs ->
                    _uiState.value = UiState.Success(dogs)
                    _showAd.value = adManager.shouldShowAd()
                }
        }
    }

    fun dismissNoInternet() {
        _showNoInternet.value = false
    }

    fun onDogClicked(dog: Dog) {
        _events.trySend(BreedListEvent.NavigateToDescription(dog))
    }

    fun onDogLongClicked(imageUrl: String) {
        _events.trySend(BreedListEvent.ExpandImage(imageUrl))
    }

    sealed interface BreedListEvent {
        data class NavigateToDescription(val dog: Dog) : BreedListEvent
        data class ExpandImage(val imageUrl: String) : BreedListEvent
    }
}
