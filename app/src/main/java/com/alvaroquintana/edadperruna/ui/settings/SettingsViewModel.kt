package com.alvaroquintana.edadperruna.ui.settings

import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics

class SettingsViewModel : ScopedViewModel() {

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_SETTINGS)
    }
}