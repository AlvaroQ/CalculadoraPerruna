package com.alvaroquintana.edadperruna.ui.result

import androidx.lifecycle.ViewModel
import com.alvaroquintana.edadperruna.core.domain.age.AgePoint
import com.alvaroquintana.edadperruna.core.domain.age.DogAgeCalculator
import com.alvaroquintana.edadperruna.core.domain.age.HumanAge
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.Screens
import com.alvaroquintana.edadperruna.managers.dogTranslateFinished
import com.alvaroquintana.edadperruna.managers.screenViewed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val calculator: DogAgeCalculator,
    private val analytics: Analytics,
) : ViewModel() {

    private val _showAd = MutableStateFlow(true)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    init {
        analytics.screenViewed(Screens.RESULT)
    }

    fun translateToHuman(years: Int, months: Int): HumanAge {
        analytics.dogTranslateFinished(years, months)
        return calculator.toHumanAge(years, months)
    }

    fun generateChartData(): List<AgePoint> = calculator.humanAgeCurve()

    fun getMarkerPoint(years: Int, months: Int): AgePoint =
        calculator.toHumanAgePoint(years, months)
}
