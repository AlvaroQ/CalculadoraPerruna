package com.alvaroquintana.edadperruna.core.domain.age

import kotlin.math.ln

class LogarithmicDogAgeCalculator : DogAgeCalculator {

    override fun toHumanAge(years: Int, months: Int): HumanAge = when {
        years == 0 && months == 0 -> HumanAge(0, 0)
        years == 0 && months == 1 -> HumanAge(1, FIRST_MONTH_HUMAN_MONTHS)
        else -> {
            val totalHuman = translate(totalDogYears(years, months))
            HumanAge(totalHuman.toInt(), ((totalHuman % 1f) * MONTHS_IN_YEAR).toInt())
        }
    }

    override fun toHumanAgePoint(years: Int, months: Int): AgePoint = when {
        years == 0 && months == 0 -> AgePoint(0f, 0f)
        years == 0 && months == 1 -> AgePoint(0f, 1f)
        else -> {
            val total = totalDogYears(years, months)
            AgePoint(total, translate(total))
        }
    }

    override fun humanAgeCurve(maxDogYears: Int): List<AgePoint> =
        (0..maxDogYears).map { i ->
            AgePoint(i.toFloat(), if (i == 0) 0f else translate(i.toFloat()))
        }

    private fun totalDogYears(years: Int, months: Int): Float =
        (years * MONTHS_IN_YEAR + months).toFloat() / MONTHS_IN_YEAR

    private fun translate(dogYears: Float): Float = LOG_SLOPE * ln(dogYears) + LOG_INTERCEPT

    private companion object {
        const val LOG_SLOPE: Float = 16f
        const val LOG_INTERCEPT: Float = 31f
        const val MONTHS_IN_YEAR: Int = 12
        const val FIRST_MONTH_HUMAN_MONTHS: Int = 1
    }
}
