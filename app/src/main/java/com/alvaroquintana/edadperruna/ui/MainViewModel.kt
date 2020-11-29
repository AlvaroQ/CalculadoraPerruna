package com.alvaroquintana.edadperruna.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.alvaroquintana.edadperruna.common.ScopedViewModel

class MainViewModel : ScopedViewModel() {

    fun setModeNight(context: Context) {
        if (getIsNightTheme(context)) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun getIsNightTheme(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean("switchNightTheme", false)
    }

    fun setIsNightTheme(context: Context, isNight: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean("switchNightTheme", isNight).apply()
    }
}