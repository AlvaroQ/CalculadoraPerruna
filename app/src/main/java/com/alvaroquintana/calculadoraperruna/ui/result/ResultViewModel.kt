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

    fun translateToHuman(years: Int, months: Int): MutableList<Int> {
        return mutableListOf(((years * 12 + months) * 7) / 12, ((years * 12 + months) * 7) % 12)
    }

    sealed class Navigation {
        object Home : Navigation()
    }
}