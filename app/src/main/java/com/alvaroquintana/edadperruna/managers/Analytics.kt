package com.alvaroquintana.edadperruna.managers

interface Analytics {
    fun logEvent(event: AnalyticsEvent)
}

fun Analytics.screenViewed(screen: String) =
    logEvent(AnalyticsEvent.ScreenViewed(screen))

fun Analytics.clicked(component: String) =
    logEvent(AnalyticsEvent.Clicked(component))

fun Analytics.dogTranslateFinished(years: Int, months: Int) =
    logEvent(AnalyticsEvent.DogTranslateFinished(years, months))

fun Analytics.appRecommendedOpen(appName: String) =
    logEvent(AnalyticsEvent.AppRecommendedOpen(appName))

object Screens {
    const val HOME = "screen_home"
    const val BREED_LIST = "screen_breed_list"
    const val BREED_DESCRIPTION = "screen_description"
    const val RESULT = "screen_result"
    const val SETTINGS = "screen_settings"
}

object Buttons {
    const val PLAY_AGAIN = "btn_play_again"
}
