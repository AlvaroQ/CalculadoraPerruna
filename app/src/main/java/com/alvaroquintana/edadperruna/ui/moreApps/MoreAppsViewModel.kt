package com.alvaroquintana.edadperruna.ui.moreApps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.domain.App
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.usecases.GetAppsRecommended
import com.alvaroquintana.usecases.GetPaymentDone
import kotlinx.coroutines.launch

class MoreAppsViewModel(private val getAppsRecommended: GetAppsRecommended,
                        private val getPaymentDone: GetPaymentDone
) : ScopedViewModel() {

    private val _progress = MutableLiveData<UiModel>()
    val progress: LiveData<UiModel> = _progress

    private val _list = MutableLiveData<MutableList<App>>()
    val list: LiveData<MutableList<App>> = _list

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_MORE_APPS)
        launch {
            _progress.value = UiModel.Loading(true)
            _list.value = appsRecommended()
            _showingAds.value = UiModel.ShowAd(!getPaymentDone())
            _progress.value = UiModel.Loading(false)
        }
    }

    private suspend fun appsRecommended(): MutableList<App> {
        return getAppsRecommended.invoke()
    }

    sealed class UiModel {
        data class Loading(val show: Boolean) : UiModel()
        data class ShowAd(val show: Boolean) : UiModel()
    }
}