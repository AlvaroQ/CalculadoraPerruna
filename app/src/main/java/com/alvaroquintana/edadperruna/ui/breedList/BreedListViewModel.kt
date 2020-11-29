package com.alvaroquintana.edadperruna.ui.breedList

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.usecases.GetBreedList
import com.alvaroquintana.usecases.GetPaymentDone
import kotlinx.coroutines.launch

class BreedListViewModel(private val getBreedList: GetBreedList,
                         private val getPaymentDone: GetPaymentDone) : ScopedViewModel() {

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _list = MutableLiveData<MutableList<Dog>>()
    val list: LiveData<MutableList<Dog>> = _list

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    fun init() {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_BREED_LIST)
        launch {
            _progress.value = true
            _list.value = getBreedList()
            _showingAds.value = UiModel.ShowAd(!getPaymentDone())
            _progress.value = false
        }
    }

    private suspend fun getBreedList(): MutableList<Dog> {
        return getBreedList.invoke()
    }

    fun onBackPressed() {
        _navigation.value = Navigation.Home(Dog(icon = "",name = ""))
    }

    fun onDogClicked(dog: Dog) {
        _navigation.value = Navigation.Home(dog)
    }

    fun onDogLongClicked(imageView: ImageView, icon: String) {
        _navigation.value = Navigation.Expand(imageView, icon)
    }

    sealed class Navigation {
        data class Home(val breed : Dog): Navigation()
        data class Expand(val imageView: ImageView, val icon: String): Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}