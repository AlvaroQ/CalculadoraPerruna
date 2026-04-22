package com.alvaroquintana.edadperruna.ui.result

import androidx.lifecycle.ViewModel
import com.alvaroquintana.edadperruna.managers.Analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.ln

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    private val _showAd = MutableStateFlow(true)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    data class ChartPoint(val dogYears: Float, val humanYears: Float)
    data class HumanAge(val years: Int, val months: Int)

    init {
        Analytics.analyticsScreenViewed(Analytics.SCREEN_RESULT)
    }

    fun translateToHuman(years: Int, months: Int): HumanAge {
        Analytics.analyticsDogTraslateFinished(years, months)
        return if (years == 0 && months == 0) {
            HumanAge(0, 0)
        } else if (years == 0 && months == 1) {
            HumanAge(1, ((1.1 % 1) * 12).toInt())
        } else {
            val totalDogsYears = (years.toFloat() * 12f + months.toFloat()) / 12f
            val totalHumanYears = translateDogAge(totalDogsYears)
            HumanAge(totalHumanYears.toInt(), ((totalHumanYears % 1) * 12).toInt())
        }
    }

    fun generateChartData(): List<ChartPoint> {
        return (0..22).map { i ->
            ChartPoint(i.toFloat(), if (i == 0) 0f else translateDogAge(i.toFloat()))
        }
    }

    fun getMarkerPoint(years: Int, months: Int): ChartPoint {
        if (years == 0 && months == 0) return ChartPoint(0f, 0f)
        if (years == 0 && months == 1) return ChartPoint(0f, 1f)
        val totalDogsYears = (years.toFloat() * 12f + months.toFloat()) / 12f
        return ChartPoint(totalDogsYears, translateDogAge(totalDogsYears))
    }

    private fun translateDogAge(number: Float): Float {
        return 16 * ln(number) + 31
    }
}
