package com.alvaroquintana.calculadoraperruna.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.calculadoraperruna.common.ScopedViewModel

class ResultViewModel() : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    fun init() {

    }

    fun navigateHome() {
        _navigation.value = Navigation.Home
    }

    sealed class Navigation {
        object Home : Navigation()
    }
}