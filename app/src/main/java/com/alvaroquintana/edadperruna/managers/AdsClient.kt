package com.alvaroquintana.edadperruna.managers

import android.app.Activity

interface AdsClient {

    fun initialize()

    fun loadRewarded(adUnitId: String)

    fun showRewardedIfAvailable(activity: Activity, onReward: () -> Unit = {})
}
