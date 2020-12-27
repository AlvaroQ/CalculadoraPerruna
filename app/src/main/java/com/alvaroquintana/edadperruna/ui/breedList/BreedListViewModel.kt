package com.alvaroquintana.edadperruna.ui.breedList

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.usecases.GetBreedList
import com.alvaroquintana.usecases.GetPaymentDone
import com.alvaroquintana.usecases.GetScreenViewer
import com.alvaroquintana.usecases.SetScreenViewer
import kotlinx.coroutines.launch

class BreedListViewModel(private val getBreedList: GetBreedList,
                         private val getPaymentDone: GetPaymentDone,
                         private val getScreenViewer: GetScreenViewer,
                         private val setScreenViewer: SetScreenViewer
) : ScopedViewModel() {

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _list = MutableLiveData<MutableList<Dog>>()
    val list: LiveData<MutableList<Dog>> = _list

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_BREED_LIST)
        launch {
            _progress.value = true
            _list.value = getBreedList()
            _showingAds.value = shouldBeShowAd()
            _progress.value = false
        }
    }

    private fun shouldBeShowAd(): UiModel.ShowAd{
        val numberScreenViewer = getScreenViewer()
        setScreenViewer(numberScreenViewer + 1)
        return if(numberScreenViewer % 3 == 0 && !getPaymentDone()) {
            UiModel.ShowAd(true)
        } else {
            UiModel.ShowAd(false)
        }
    }

    private suspend fun getBreedList(): MutableList<Dog> {
        return getBreedList.invoke()
    }

    fun onDogClicked(idBreed: Int, dog: Dog) {
        _navigation.value = Navigation.BreedDescription(idBreed, dog)
    }

    fun onDogLongClicked(imageView: ImageView, icon: String) {
        _navigation.value = Navigation.Expand(imageView, icon)
    }

    sealed class Navigation {
        data class BreedDescription(val idBreed: Int, val breed : Dog): Navigation()
        data class Expand(val imageView: ImageView, val image: String): Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}