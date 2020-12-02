package com.alvaroquintana.edadperruna.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.alvaroquintana.data.source.SharedPreferencesLocalDataSource

open class SharedPrefsDataSource (context: Context): SharedPreferencesLocalDataSource {
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override var paymentDone: Boolean
        get() = sharedPreferences.getBoolean(PAYMENT_DONE, false)
        set(value) = sharedPreferences.edit().putBoolean(PAYMENT_DONE, value).apply()

    override var isNightTheme: Boolean
        get() = sharedPreferences.getBoolean(SWITCH_NIGHT_THEME, true)
        set(value) = sharedPreferences.edit().putBoolean(SWITCH_NIGHT_THEME, value).apply()

    companion object {
        const val PAYMENT_DONE = "payment_done"
        const val SWITCH_NIGHT_THEME = "switch_night_theme"
    }
}