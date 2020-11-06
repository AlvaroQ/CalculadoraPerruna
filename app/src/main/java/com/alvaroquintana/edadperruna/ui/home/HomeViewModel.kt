package com.alvaroquintana.edadperruna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.managers.Analytics

class HomeViewModel : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val mError = MutableLiveData<Error>()

    val error: LiveData<Error> = mError
    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_HOME)
    }

    fun navigateToBreedList() {
        _navigation.value = Navigation.BreedList
    }

    fun navigateToResult(dog: Dog) {
        Analytics.analyticsClicked(Analytics.BTN_RESULT)
        _navigation.value = Navigation.Result(dog)
    }

    fun checkErrors(dog: Dog, year: String, month: String) : Boolean {
        var isCorrect = true

        if(dog.name == "") {
            handleFailure(Error.ErrorBreedEmpty)
            isCorrect = false
        }
        if(year == "") {
            handleFailure(Error.ErrorYearEmpty)
            isCorrect = false
        }
        if(month == "") {
            handleFailure(Error.ErrorMonthEmpty)
            isCorrect = false
        }
        if(month != "" && (month.toInt() < 0 || month.toInt() > 12)) {
            handleFailure(Error.ErrorMonthIlegal)
            isCorrect = false
        }

        return isCorrect
    }

    private fun handleFailure(error: Error) {
        when (error) {
            is Error.ErrorBreedEmpty -> mError.value = Error.ErrorBreedEmpty
            is Error.ErrorYearEmpty -> mError.value = Error.ErrorYearEmpty
            is Error.ErrorMonthEmpty -> mError.value = Error.ErrorMonthEmpty
            is Error.ErrorMonthIlegal -> mError.value = Error.ErrorMonthIlegal
        }
    }

    sealed class Navigation {
        data class Result(val breed : Dog): Navigation()
        object BreedList : Navigation()
    }

    sealed class Error {
        object ErrorBreedEmpty : Error()
        object ErrorYearEmpty : Error()
        object ErrorMonthEmpty : Error()
        object ErrorMonthIlegal : Error()
    }
}