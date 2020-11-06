package com.alvaroquintana.edadperruna.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.domain.App
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.usecases.GetAppsRecommended
import kotlinx.coroutines.launch

class ResultViewModel(private val getAppsRecommended: GetAppsRecommended) : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _list = MutableLiveData<MutableList<App>>()
    val list: LiveData<MutableList<App>> = _list

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_RESULT)
        launch {
            _progress.value = true
            _list.value = appsRecommended()
            _progress.value = false
        }
    }

    private suspend fun appsRecommended(): MutableList<App> {
        return getAppsRecommended.invoke()
    }

    fun onAppClicked(url: String) {
        Analytics.analyticsAppRecommendedOpen(url)
        _navigation.value = Navigation.Open(url)
    }

    fun navigateHome() {
        _navigation.value = Navigation.Home
    }

    fun translateToHuman(years: Int, months: Int): MutableList<Int> {
        Analytics.analyticsDogTraslateFinished(years, months)
        return mutableListOf(((years * 12 + months) * 7) / 12, ((years * 12 + months) * 7) % 12)
    }

    sealed class Navigation {
        object Home : Navigation()
        data class Open(val url : String): Navigation()
    }

    sealed class UiModel {
        data class Loading(val show: Boolean) : UiModel()
    }
}