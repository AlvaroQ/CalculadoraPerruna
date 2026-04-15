package com.alvaroquintana.edadperruna.managers

import android.content.Context
import android.os.Bundle
import com.alvaroquintana.edadperruna.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Typed analytics manager — replaces the old static [Analytics] object.
 * The old object is kept temporarily as a facade that delegates here,
 * so existing call sites don't break until they're migrated.
 */
@Singleton
class AnalyticsManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val firebase: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            putString("uid", getUID())
            putString("app_version", BuildConfig.VERSION_NAME)
            putString("app_name", BuildConfig.APPLICATION_ID)
            event.params.forEach { (key, value) -> putString(key, value) }
        }
        firebase.logEvent(event.eventName, bundle)
    }

    private fun getUID(): String =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
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

/**
 * Legacy facade — keeps existing static call sites working.
 * Will be removed when all ViewModels inject [AnalyticsManager] directly.
 */
object Analytics {
    private var manager: AnalyticsManager? = null

    fun initialize(context: Context) {
        // No-op: AnalyticsManager is now @Singleton via Hilt.
        // This keeps CalculadoraApp.onCreate() from crashing.
    }

    internal fun setManager(analyticsManager: AnalyticsManager) {
        manager = analyticsManager
    }

    fun analyticsScreenViewed(screenTitle: String) {
        manager?.logEvent(AnalyticsEvent.ScreenViewed(screenTitle))
    }

    fun analyticsDogTraslateFinished(years: Int, months: Int) {
        manager?.logEvent(AnalyticsEvent.DogTranslateFinished(years, months))
    }

    fun analyticsClicked(btnDescription: String) {
        manager?.logEvent(AnalyticsEvent.Clicked(btnDescription))
    }

    fun analyticsAppRecommendedOpen(appName: String) {
        manager?.logEvent(AnalyticsEvent.AppRecommendedOpen(appName))
    }

    // Screen constants
    const val SCREEN_BREED_LIST = "screen_breed_list"
    const val SCREEN_RESULT = "screen_result"
    const val SCREEN_HOME = "screen_home"
    const val SCREEN_SETTINGS = "screen_settings"
    const val SCREEN_DESCRIPTION = "screen_description"

    const val BTN_RESULT = "btn_play_again"
}
