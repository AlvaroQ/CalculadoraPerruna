package com.alvaroquintana.edadperruna.core.domain.age

interface DogAgeCalculator {

    fun toHumanAge(years: Int, months: Int): HumanAge

    fun toHumanAgePoint(years: Int, months: Int): AgePoint

    fun humanAgeCurve(maxDogYears: Int = DEFAULT_CURVE_MAX_YEARS): List<AgePoint>

    companion object {
        const val DEFAULT_CURVE_MAX_YEARS: Int = 22
    }
}
