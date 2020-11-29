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
import com.alvaroquintana.edadperruna.ui.home.HomeFragmentDirections
import com.alvaroquintana.edadperruna.ui.settings.SettingsFragment
import com.alvaroquintana.edadperruna.utils.log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

class MainActivity : BaseActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)
    private lateinit var navController : NavController
    private val mainViewModel: MainViewModel by lifecycleScope.viewModel(this)

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var rewardedAd: RewardedAd
    lateinit var activity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel.setModeNight(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        activity = this
        loadInstersticialAd()
        navController = findNavController(R.id.nav_host_fragment)
    }

    fun setupToolbar(title: String, hasBackButton: Boolean, myBackFunction: () -> Unit = {}) {
        binding.toolbarTitle.text = title
        if(hasBackButton) {
            binding.backButton.visibility = View.VISIBLE
            binding.backButton.setOnClickListener { myBackFunction() }
        } else {
            binding.backButton.visibility = View.INVISIBLE
        }

        val imagenSettings: ImageView = binding.imagenSettings
        imagenSettings.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_home_to_settings)
        }
    }

    fun setupBackground(screen: Screen) {
        when(screen) {
            Screen.MAIN -> {
                binding.imageBackground.visibility = View.VISIBLE
                binding.imageBackground.setImageDrawable(getDrawable(R.drawable.wallpaper_main))
            }
            Screen.BREED_LIST -> {
                binding.imageBackground.visibility = View.GONE
            }
            Screen.RESULT -> {
                binding.imageBackground.visibility = View.VISIBLE
                binding.imageBackground.setImageDrawable(getDrawable(R.drawable.wallpaper_result))
                showAd(false)
            }
            Screen.SETTINGS -> {
                binding.imageBackground.visibility = View.GONE
            }
            Screen.DESCRIPTION -> {
                binding.imageBackground.visibility = View.GONE
            }
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
    }

    fun showRewardAd() {
        rewardedAd = RewardedAd(activity, getString(R.string.BONIFICADO_LIST))
        val adLoadCallback: RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                rewardedAd.show(activity, null)
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                FirebaseCrashlytics.getInstance().recordException(Throwable(adError.message))
                log("ResultActivity - loadAd", "Ad failed to load.")
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) super.onBackPressed()
        else supportFragmentManager.popBackStack()
    }

    enum class Screen {
        MAIN, BREED_LIST, RESULT, DESCRIPTION, SETTINGS
    }
}