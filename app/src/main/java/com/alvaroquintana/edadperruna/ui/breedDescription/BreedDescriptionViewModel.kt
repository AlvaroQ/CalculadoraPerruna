package com.alvaroquintana.edadperruna.ui.breedDescription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.usecases.GetBreedsDescription
import com.alvaroquintana.usecases.GetScreenViewer
import com.alvaroquintana.usecases.SetScreenViewer
import kotlinx.coroutines.launch

class BreedDescriptionViewModel(private val getBreedDescription: GetBreedsDescription,
                                private val getScreenViewer: GetScreenViewer,
                                private val setScreenViewer: SetScreenViewer
) : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _breedData = MutableLiveData<Dog?>()
    val breedData: LiveData<Dog?> = _breedData

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_RESULT)
        _progress.value = true
        _showingAds.value = shouldBeShowAd()
        _progress.value = false
    }

    private fun shouldBeShowAd(): UiModel.ShowAd{
        val numberScreenViewer = getScreenViewer()
        setScreenViewer(numberScreenViewer + 1)
        return if(numberScreenViewer != 0 && numberScreenViewer % 4 == 0) {
            UiModel.ShowAd(true)
        } else {
            UiModel.ShowAd(false)
        }
    }

    fun getDescription(breedId: String) {
        launch {
            val breedDescription = getBreedDescription.invoke(breedId)
            _breedData.value = breedDescription
        }
    }

    fun navigateToHome(dog: Dog) {
        _navigation.value = Navigation.Home(dog)
    }

    sealed class Navigation {
        data class Home(val breed : Dog): Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}