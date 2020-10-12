package com.alvaroquintana.edadperruna.ui

import androidx.appcompat.app.AppCompatDelegate
import com.alvaroquintana.edadperruna.common.ScopedViewModel

class MainViewModel() : ScopedViewModel() {

    fun setModeNight() {
        if (getIsNightTheme()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun getIsNightTheme(): Boolean {
        return false
    }

}