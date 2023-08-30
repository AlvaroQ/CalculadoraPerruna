package com.alvaroquintana.edadperruna.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaroquintana.edadperruna.common.ScopedViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import kotlin.math.ln

class ResultViewModel() : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    private var marker: MutableList<Float> = mutableListOf()

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_RESULT)
        launch {
            _progress.value = true
            _showingAds.value = UiModel.ShowAd(true)
            _progress.value = false
        }
    }

    fun navigateHome() {
        _navigation.value = Navigation.Home
    }

    fun onDogLongClicked() {
        _navigation.value = Navigation.Expand
    }

    /** 16 * ln(dog age) + 31 = human age.
        Using that equation:
     1-year-old dog is like a 31-year-old human
     3-year-old dog is like a 49-year-old humana
     7-year-old dog is like a 62-year-old human */
    fun translateToHuman(years: Int, months: Int): MutableList<Int> {
        Analytics.analyticsDogTraslateFinished(years, months)
        return if(years == 0 && months == 0) {
            marker = mutableListOf(0f, 0f)
            mutableListOf(0.0.toInt(), 0.0.toInt())
        } else if(years == 0 && months == 1) {
            marker = mutableListOf(0f, 1f)
            mutableListOf(1.1.toInt(), ((1.1 % 1)*12).toInt())
        } else {
            val totalDogsYears: Float = (((years.toFloat() * 12f + months.toFloat()) / 12f))
            val totalHumanYears = translateDogAge(totalDogsYears)
            marker = mutableListOf(totalDogsYears, totalHumanYears)
            mutableListOf(totalHumanYears.toInt(), ((totalHumanYears % 1)*12).toInt())
        }
    }

    fun generateEntries(): ArrayList<Entry> {
        val yValues: ArrayList<Entry> = ArrayList()
        yValues.add(Entry(0f, 0f))
        for(i in 1..22) yValues.add(Entry(i.toFloat(), translateDogAge(i.toFloat())))
        return yValues
    }

    private fun translateDogAge(number: Float): Float {
        return 16 * (ln(number)) + 31
    }

    sealed class Navigation {
        object Home : Navigation()
        object Expand: Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}