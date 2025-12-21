package com.alvaroquintana.edadperruna.managers

import android.content.Context
import android.os.Bundle
import com.alvaroquintana.edadperruna.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

object Analytics {
    private var mFirebase: FirebaseAnalytics? = null
    private var isInitialized = false

    fun initialize(ctx: Context) {
        if (!isInitialized) {
            mFirebase = FirebaseAnalytics.getInstance(ctx.applicationContext)
            isInitialized = true
        }
    }

    private fun getUID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun analyticsScreenViewed(screenTitle: String) {
        if (!isInitialized) return
        logEvent(
            Event("screen_viewed")
            .with("uid", getUID())
            .with("screen_title", screenTitle)
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    fun analyticsDogTraslateFinished(years: Int, months: Int) {
        if (!isInitialized) return
        logEvent(
            Event("game_finished")
            .with("uid", getUID())
            .with("years", years.toString())
            .with("months", months.toString())
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    fun analyticsClicked(btnDescription: String) {
        if (!isInitialized) return
        logEvent(
            Event("clicked")
            .with("uid", getUID())
            .with("component", btnDescription)
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    fun analyticsAppRecommendedOpen(appName: String) {
        if (!isInitialized) return
        logEvent(
            Event("app_recommended_open")
            .with("uid", getUID())
            .with("app_name", appName)
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    private fun logEvent(event: Event) {
        mFirebase?.logEvent(event.eventName, event.bundle)
    }

    private class Event(val eventName: String) {
        val bundle = Bundle()
        fun with(key: String, value: String): Event {
            bundle.putString(key, value)
            return this
        }
    }

    // SCREENS
    const val SCREEN_BREED_LIST = "screen_game"
    const val SCREEN_RESULT = "screen_result"
    const val SCREEN_HOME = "screen_ranking"
    const val SCREEN_SETTINGS = "screen_settings"
    const val SCREEN_DESCRIPTION = "screen_description"
    const val SCREEN_MORE_APPS = "screen_more_apps"

    const val SHOW_AD_INTERSTICIAL = "show_instersticial_ad"
    const val SHOW_AD_BONIFICATION = "show_bonification_ad"

    // CLICKED
    const val BTN_RESULT = "btn_play_again"
}