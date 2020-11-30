package com.alvaroquintana.edadperruna.managers

import android.content.Context
import android.os.Bundle
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.base.BaseActivity
import com.google.firebase.analytics.FirebaseAnalytics

object Analytics {
    lateinit var mFirebase: FirebaseAnalytics
    lateinit var ctx: Context

    fun initialize(ctx: Context) {
        Analytics.ctx = ctx
        mFirebase = FirebaseAnalytics.getInstance(ctx.applicationContext)
    }

    fun analyticsScreenViewed(screenTitle: String) {

        logEvent(
            Event("screen_viewed")
            .with("uid", (ctx as BaseActivity).getUID())
            .with("screen_title", screenTitle)
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    fun analyticsDogTraslateFinished(years: Int, months: Int) {

        logEvent(
            Event("game_finished")
            .with("uid", (ctx as BaseActivity).getUID())
            .with("years", years.toString())
            .with("months", months.toString())
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    fun analyticsClicked(btnDescription: String) {

        logEvent(
            Event("clicked")
            .with("uid", (ctx as BaseActivity).getUID())
            .with("component", btnDescription)
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    fun analyticsAppRecommendedOpen(appName: String) {

        logEvent(
            Event("app_recommended_open")
            .with("uid", (ctx as BaseActivity).getUID())
            .with("app_name", appName)
            .with("app_version", BuildConfig.VERSION_NAME)
            .with("app_name", BuildConfig.APPLICATION_ID))
    }

    private fun logEvent(event: Event) {
        mFirebase.logEvent(event.eventName, event.bundle)
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

    // CLICKED
    const val BTN_RESULT = "btn_play_again"
}