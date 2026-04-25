package com.alvaroquintana.edadperruna.application

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class CalculadoraApp : Application() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        initFirebaseAuth()
        applyTheme()
    }

    private fun initFirebaseAuth() {
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInAnonymously()
                .addOnFailureListener { error ->
                    Log.e("CalculadoraApp", "Anonymous auth failed", error)
                    crashlytics.recordException(error)
                }
        }
    }

    private fun applyTheme() {
        appScope.launch {
            val themeValue = preferencesRepository.themeMode.first()
            val mode = when (themeValue) {
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
