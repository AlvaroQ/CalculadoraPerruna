package com.alvaroquintana.edadperruna.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.navigation.AppNavigation
import com.alvaroquintana.edadperruna.ui.theme.CalculadoraPerrunaTheme
import com.alvaroquintana.edadperruna.utils.showBonificado
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize Ads
        MobileAds.initialize(this)
        loadRewardedAd()

        setContent {
            CalculadoraPerrunaTheme {
                AppNavigation()
            }
        }
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            this,
            getString(R.string.BONIFICADO_LIST),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("MainActivity", "Ad failed to load: ${adError.message}")
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("MainActivity", "Ad was loaded.")
                    rewardedAd = ad
                }
            }
        )
    }

    fun showRewardAd(show: Boolean) {
        showBonificado(this, show, rewardedAd)
    }
}
