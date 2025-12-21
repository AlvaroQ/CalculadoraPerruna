package com.alvaroquintana.edadperruna.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
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
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : ComponentActivity() {
    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Apply saved theme preference
        applyThemeFromPreferences()

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

    private fun applyThemeFromPreferences() {
        val preferences = getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)
        val themeValue = preferences.getString("theme_mode", "system")
        val mode = when (themeValue) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            this,
            getString(R.string.BONIFICADO_LIST),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("MainActivity", adError.toString())
                    FirebaseCrashlytics.getInstance().recordException(Throwable(adError.message))
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
