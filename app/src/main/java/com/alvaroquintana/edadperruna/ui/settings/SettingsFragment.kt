package com.alvaroquintana.edadperruna.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.MainActivity
import com.alvaroquintana.edadperruna.utils.Constants
import com.alvaroquintana.edadperruna.utils.rateApp
import com.alvaroquintana.edadperruna.utils.shareApp
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        // more_apps
        val moreApps: Preference? = findPreference("more_apps")
        moreApps?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            try {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_MORE_APPS)))
            } catch (_: ActivityNotFoundException) { }
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setupToolbar(getString(R.string.settings), hasSettings = false, hasBackButton = true)
    }
}