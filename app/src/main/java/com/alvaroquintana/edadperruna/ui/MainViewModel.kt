package com.alvaroquintana.edadperruna.ui

import androidx.appcompat.app.AppCompatDelegate
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.usecases.GetNightTheme

class MainViewModel(private val getIsNightTheme: GetNightTheme) : ScopedViewModel() {

    fun setModeNight() {
        if (getIsNightTheme())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}