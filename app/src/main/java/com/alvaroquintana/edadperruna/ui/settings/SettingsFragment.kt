package com.alvaroquintana.edadperruna.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.utils.log
import com.alvaroquintana.edadperruna.utils.rateApp
import com.alvaroquintana.edadperruna.utils.shareApp
import com.android.billingclient.api.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class SettingsFragment : PreferenceFragmentCompat(), PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient
    private val skuList = listOf("remove_ad")

    private val settingsViewModel: SettingsViewModel by lifecycleScope.viewModel(this)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBillingClient()

        // rate_app
        val rateApp: Preference? = findPreference("rate_app")
        rateApp?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            rateApp(requireContext())
            false
        }

        // share
        val share: Preference? = findPreference("share")
        share?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            shareApp(-1, requireContext())
            false
        }

        // Version
        val version: Preference? = findPreference("version")
        version?.summary = "${getString(R.string.settings_version)} ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"

        // deleteAds
        val deleteAds: Preference? = findPreference("delete_ads")
        if(settingsViewModel.getPaymentDone()) deleteAds?.isVisible = false
        deleteAds?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            loadAllSKUs()
            false
        }

        // more_apps
        val moreApps: Preference? = findPreference("more_apps")
        moreApps?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            settingsViewModel.navigationToMoreApps()
            false
        }

        // update_breeds
        val updateBreeds: Preference? = findPreference("update_breeds")
        if(BuildConfig.uploadBreedsFromJSON) updateBreeds?.isVisible = true
        updateBreeds?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            settingsViewModel.updateBreeds()
            false
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setupToolbar(getString(R.string.settings), hasSettings = false, hasBackButton = true)
        settingsViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
        settingsViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
    }

    private fun loadAd(model: SettingsViewModel.UiModel) {
        if (model is SettingsViewModel.UiModel.ShowAd) (activity as MainActivity).showAd(model.show)
    }

    private fun navigate(navigation: SettingsViewModel.Navigation?) {
        (activity as MainActivity).apply {
            when (navigation) {
                is SettingsViewModel.Navigation.Home -> {
                    val action = SettingsFragmentDirections.actionNavigationSettingsToHome("", "")
                    findNavController().navigate(action)
                }
                is SettingsViewModel.Navigation.MoreApps -> {
                    val action = SettingsFragmentDirections.actionNavigationSettingsToMoreApps()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener(this)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                log("Billing", "billingResult.responseCode = " + billingResult.responseCode)
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Analytics.analyticsScreenViewed("Billing setup finished")
                }
            }

            override fun onBillingServiceDisconnected() {
                log("BILLING", "Billing Service Disconnected")
                Analytics.analyticsScreenViewed("Billing Service Disconnected")
            }
        })
    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                for (skuDetails in skuDetailsList) {
                    if (skuDetails.sku == "remove_ad") {
                        val billingFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(skuDetails)
                            .build()
                        billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
                    }
                }
            }
        }

    } else {
        log("BILLING", "Billing Client not ready")
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Analytics.analyticsScreenViewed("billing_purchase_ok")
            settingsViewModel.savePaymentDone()
            for (purchase in purchases) {
                acknowledgePurchase(purchase.purchaseToken)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            log("BILLING", "Billing USER_CANCELED")
            Analytics.analyticsScreenViewed("billing_purchase_canceled")

        } else {
            // Handle any other error codes.
            log("BILLING", "Billing errors")
            Analytics.analyticsScreenViewed("billing_purchase_error")
        }
    }


    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage

            Analytics.analyticsScreenViewed("acknowledgePurchase responseCode=$responseCode")
            Analytics.analyticsScreenViewed("acknowledgePurchase debugMessage=$debugMessage")
        }
    }
}