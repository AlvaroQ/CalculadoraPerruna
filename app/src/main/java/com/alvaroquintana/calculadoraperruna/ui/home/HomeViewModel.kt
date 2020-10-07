package com.alvaroquintana.calculadoraperruna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.calculadoraperruna.common.ScopedViewModel

class HomeViewModel : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    fun navigateToBreedList() {
        _navigation.value = Navigation.BreedList
    }

    fun navigateToResult() {
        _navigation.value = Navigation.Result("-1")
    }

    sealed class Navigation {
        data class Result(val dogId : String): Navigation()
        object BreedList : Navigation()
    }
}