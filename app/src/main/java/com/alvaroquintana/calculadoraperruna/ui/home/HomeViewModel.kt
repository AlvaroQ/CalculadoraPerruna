package com.alvaroquintana.calculadoraperruna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.calculadoraperruna.common.ScopedViewModel
import com.alvaroquintana.domain.Dog

class HomeViewModel : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    fun navigateToBreedList() {
        _navigation.value = Navigation.BreedList
    }

    fun navigateToResult(dog: Dog) {
        _navigation.value = Navigation.Result(dog)
    }

    sealed class Navigation {
        data class Result(val breed : Dog): Navigation()
        object BreedList : Navigation()
    }
}