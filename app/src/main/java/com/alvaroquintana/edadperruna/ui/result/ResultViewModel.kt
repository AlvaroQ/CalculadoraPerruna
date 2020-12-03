package com.alvaroquintana.edadperruna.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.domain.App
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionViewModel
import com.alvaroquintana.usecases.GetAppsRecommended
import com.alvaroquintana.usecases.GetPaymentDone
import kotlinx.coroutines.launch

class ResultViewModel(private val getPaymentDone: GetPaymentDone) : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_RESULT)
        launch {
            _progress.value = true
            _showingAds.value = UiModel.ShowAd(!getPaymentDone())
            _progress.value = false
        }
    }

    fun navigateHome() {
        _navigation.value = Navigation.Home
    }

    fun onDogLongClicked() {
        _navigation.value = Navigation.Expand
    }

    fun translateToHuman(years: Int, months: Int): MutableList<Int> {
        Analytics.analyticsDogTraslateFinished(years, months)
        return mutableListOf(((years * 12 + months) * 7) / 12, ((years * 12 + months) * 7) % 12)
    }

    sealed class Navigation {
        object Home : Navigation()
        object Expand: Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}