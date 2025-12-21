package com.alvaroquintana.edadperruna.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.alvaroquintana.edadperruna.managers.Analytics
import com.google.firebase.auth.FirebaseAuth

class CalculadoraApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
        initFirebaseAuth()
        Analytics.initialize(this)
        applyTheme()
    }

    private fun initFirebaseAuth() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
        }
    }

    private fun applyTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themeValue = sharedPreferences.getString("theme_mode", "system")

        val mode = when (themeValue) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
