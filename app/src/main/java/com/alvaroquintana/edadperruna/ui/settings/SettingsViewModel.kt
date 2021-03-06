package com.alvaroquintana.edadperruna.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.usecases.GetPaymentDone
import com.alvaroquintana.usecases.SetPaymentDone
import com.alvaroquintana.usecases.UpdateBreedDescription
import kotlinx.coroutines.launch

class SettingsViewModel(private val setPaymentDone: SetPaymentDone,
                        val getPaymentDone: GetPaymentDone,
                        private val updateBreedDescription: UpdateBreedDescription) : ScopedViewModel() {

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_SETTINGS)
        _showingAds.value = UiModel.ShowAd(!getPaymentDone())
    }

    fun savePaymentDone() {
        launch {
            // remove_ad
            setPaymentDone.invoke(true)
            _showingAds.value = UiModel.ShowAd(false)
        }
    }

    fun navigationToMoreApps() {
        _navigation.value = Navigation.MoreApps
    }

    fun updateBreeds() {
        launch {
            updateBreedDescription.invoke()
        }
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }

    sealed class Navigation {
        object Home: Navigation()
        object MoreApps: Navigation()
    }
}