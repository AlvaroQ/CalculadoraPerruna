package com.alvaroquintana.edadperruna.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.navigation.AppNavigation
import com.alvaroquintana.edadperruna.core.designsystem.theme.CalculadoraPerrunaTheme
import com.alvaroquintana.edadperruna.managers.AdsClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var adsClient: AdsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        adsClient.initialize()
        adsClient.loadRewarded(getString(R.string.BONIFICADO_LIST))

        setContent {
            CalculadoraPerrunaTheme {
                AppNavigation()
            }
        }
    }

    fun showRewardAd(show: Boolean) {
        if (show) adsClient.showRewardedIfAvailable(this)
    }
}
