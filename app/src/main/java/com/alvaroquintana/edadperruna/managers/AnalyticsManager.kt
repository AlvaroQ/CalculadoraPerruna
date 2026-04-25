package com.alvaroquintana.edadperruna.managers

import android.os.Bundle
import com.alvaroquintana.edadperruna.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebase: FirebaseAnalytics,
    private val firebaseAuth: FirebaseAuth,
) : Analytics {

    override fun logEvent(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            putString("uid", getUID())
            putString("app_version", BuildConfig.VERSION_NAME)
            putString("app_name", BuildConfig.APPLICATION_ID)
            event.params.forEach { (key, value) -> putString(key, value) }
        }
        firebase.logEvent(event.eventName, bundle)
    }

    private fun getUID(): String =
        firebaseAuth.currentUser?.uid ?: ""
}

sealed interface AnalyticsEvent {
    val eventName: String
    val params: Map<String, String>

    data class ScreenViewed(val screenTitle: String) : AnalyticsEvent {
        override val eventName = "screen_viewed"
        override val params = mapOf("screen_title" to screenTitle)
    }

    data class DogTranslateFinished(val years: Int, val months: Int) : AnalyticsEvent {
        override val eventName = "game_finished"
        override val params = mapOf("years" to years.toString(), "months" to months.toString())
    }

    data class Clicked(val component: String) : AnalyticsEvent {
        override val eventName = "clicked"
        override val params = mapOf("component" to component)
    }

    data class AppRecommendedOpen(val appName: String) : AnalyticsEvent {
        override val eventName = "app_recommended_open"
        override val params = mapOf("app_name" to appName)
    }
}
