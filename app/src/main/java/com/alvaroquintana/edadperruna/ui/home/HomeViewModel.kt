package com.alvaroquintana.edadperruna.ui.home

import androidx.lifecycle.ViewModel
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.Buttons
import com.alvaroquintana.edadperruna.managers.Screens
import com.alvaroquintana.edadperruna.managers.clicked
import com.alvaroquintana.edadperruna.managers.screenViewed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val analytics: Analytics,
) : ViewModel() {

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        analytics.screenViewed(Screens.HOME)
    }

    fun navigateToBreedList() {
        _events.trySend(HomeEvent.NavigateToBreedList)
    }

    fun navigateToResult(dog: Dog) {
        analytics.clicked(Buttons.PLAY_AGAIN)
        _events.trySend(HomeEvent.NavigateToResult(dog))
    }

    fun checkErrors(dog: Dog, year: String, month: String): Boolean {
        var isCorrect = true

        if (dog.name.isNullOrEmpty()) {
            _events.trySend(HomeEvent.ShowError(HomeError.BreedEmpty))
            isCorrect = false
        }
        if (year.isEmpty()) {
            _events.trySend(HomeEvent.ShowError(HomeError.YearEmpty))
            isCorrect = false
        }
        if (month.isEmpty()) {
            _events.trySend(HomeEvent.ShowError(HomeError.MonthEmpty))
            isCorrect = false
        }
        val monthInt = month.toIntOrNull()
        if (month.isNotEmpty() && (monthInt == null || monthInt < 0 || monthInt > 11)) {
            _events.trySend(HomeEvent.ShowError(HomeError.MonthIllegal))
            isCorrect = false
        }

        return isCorrect
    }

    sealed interface HomeEvent {
        data class NavigateToResult(val breed: Dog) : HomeEvent
        data object NavigateToBreedList : HomeEvent
        data class ShowError(val error: HomeError) : HomeEvent
    }

    sealed interface HomeError {
        data object BreedEmpty : HomeError
        data object YearEmpty : HomeError
        data object MonthEmpty : HomeError
        data object MonthIllegal : HomeError
    }
}
