package com.alvaroquintana.edadperruna.managers

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.alvaroquintana.edadperruna.utils.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobAdsClient @Inject constructor(
    @ApplicationContext private val context: Context,
) : AdsClient {

    @VisibleForTesting
    internal var rewardedAd: RewardedAd? = null

    override fun initialize() {
        MobileAds.initialize(context)
    }

    override fun loadRewarded(adUnitId: String) {
        RewardedAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    log(TAG, "Rewarded ad failed to load: ${adError.message}")
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    log(TAG, "Rewarded ad loaded.")
                    rewardedAd = ad
                }
            },
        )
    }

    override fun showRewardedIfAvailable(activity: Activity, onReward: () -> Unit) {
        rewardedAd?.show(activity) { rewardItem ->
            Log.d(TAG, "User earned reward. amount=${rewardItem.amount} type=${rewardItem.type}")
            onReward()
        } ?: log(TAG, "Rewarded ad not ready.")
    }

    private companion object {
        const val TAG = "AdMobAdsClient"
    }
}
