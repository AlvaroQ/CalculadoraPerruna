package com.alvaroquintana.edadperruna.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.base.BaseActivity
import com.alvaroquintana.edadperruna.common.viewBinding
import com.alvaroquintana.edadperruna.databinding.MainActivityBinding
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.utils.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : BaseActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)
    private lateinit var navController : NavController
    var appOpened = false

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var rewardedAd: RewardedAd
    lateinit var activity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        activity = this
        loadInstersticialAd()
        navController = findNavController(R.id.nav_host_fragment)
    }

    fun setupToolbar(title: String, hasSettings: Boolean, hasBackButton: Boolean = false) {
        binding.appBarLayoutMain.visibility = View.VISIBLE
        binding.toolbarTitle.text = title
        if(hasBackButton) {
            binding.backButton.visibility = View.VISIBLE
            binding.backButton.setOnClickListener { onBackPressed() }
        } else {
            binding.backButton.visibility = View.INVISIBLE
        }

        if(hasSettings) {
            binding.imagenSettings.visibility = View.VISIBLE
            val imagenSettings: ImageView = binding.imagenSettings
            imagenSettings.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_home_to_settings)
            }
        } else {
            binding.imagenSettings.visibility = View.GONE
        }


        when (title) {
            getString(R.string.settings) -> binding.imageBackground.setImageDrawable(getDrawable(R.drawable.splash_gradient))
            getString(R.string.choose_breed) -> binding.appBarLayoutMain.visibility = View.GONE
            else -> binding.imageBackground.setImageDrawable(getDrawable(R.drawable.wallpaper_main))
        }
    }

    fun showAd(show: Boolean) {
        if(show) {
            MobileAds.initialize(this)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        } else {
            binding.adView.visibility = View.GONE
        }
    }

    fun showInstersticialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            log("TAG", "The interstitial wasn't loaded yet.")
            FirebaseCrashlytics.getInstance().recordException(Throwable("The interstitial wasn't loaded"))
        }
    }

    private fun loadInstersticialAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.INTERSTICIAL_RESULT)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        Analytics.analyticsScreenViewed(Analytics.SHOW_AD_INTERSTICIAL)
    }

    fun showRewardAd() {
        rewardedAd = RewardedAd(activity, getString(R.string.BONIFICADO_LIST))
        val adLoadCallback: RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                rewardedAd.show(activity, null)
                Analytics.analyticsScreenViewed(Analytics.SHOW_AD_BONIFICATION)
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                FirebaseCrashlytics.getInstance().recordException(Throwable(adError.message))
                log("ResultActivity - loadAd", "Ad failed to load.")
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    override fun onBackPressed() {
        when(navController.currentDestination?.id) {
            R.id.navigation_more_apps -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_more_apps_to_settings)
            }
            R.id.navigation_settings -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_settings_to_home)
            }
            R.id.navigation_breed_list -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_breed_list_to_home)
            }
            R.id.navigation_result -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_result_to_home)
            }
            R.id.navigation_breed_description -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_breed_description_to_breed_list)
            }
            else -> finish()
        }
    }

    enum class Screen {
        MAIN, BREED_LIST, RESULT, DESCRIPTION, SETTINGS
    }
}