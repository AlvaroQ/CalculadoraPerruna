package com.alvaroquintana.edadperruna.ui.breedDescription

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.ui.breedList.BreedListViewModel
import com.alvaroquintana.usecases.GetBreedsDescription
import com.alvaroquintana.usecases.GetPaymentDone
import kotlinx.coroutines.launch

class BreedDescriptionViewModel(private val getPaymentDone: GetPaymentDone,
                                private val getBreedDescription: GetBreedsDescription) : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _breedData = MutableLiveData<Dog>()
    val breedData: LiveData<Dog> = _breedData

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_RESULT)
        _progress.value = true
        _showingAds.value = UiModel.ShowAd(!getPaymentDone())
        _progress.value = false
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

    fun onDogLongClicked() {
        _navigation.value = Navigation.Expand
    }

    sealed class Navigation {
        data class Home(val breed : Dog): Navigation()
        object Expand: Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}