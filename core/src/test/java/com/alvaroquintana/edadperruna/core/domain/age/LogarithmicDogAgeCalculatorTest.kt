package com.alvaroquintana.edadperruna.core.domain.age

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LogarithmicDogAgeCalculatorTest {

    private val calculator = LogarithmicDogAgeCalculator()

    // ---------- toHumanAge ----------

    @Test
    fun `toHumanAge with 0 years and 0 months returns 0`() {
        val result = calculator.toHumanAge(0, 0)
        assertEquals(HumanAge(0, 0), result)
    }

    @Test
    fun `toHumanAge with 0 years and 1 month returns 1 year`() {
        val result = calculator.toHumanAge(0, 1)
        assertEquals(1, result.years)
    }

    @Test
    fun `toHumanAge with 1 year and 0 months returns 31`() {
        val result = calculator.toHumanAge(1, 0)
        assertEquals(31, result.years)
    }

    @Test
    fun `toHumanAge with 7 years returns approximately 62`() {
        val result = calculator.toHumanAge(7, 0)
        // 16 * ln(7) + 31 ≈ 62.12
        assertEquals(62, result.years)
    }

    @Test
    fun `toHumanAge with 2 years and 6 months computes human months`() {
        val result = calculator.toHumanAge(2, 6)
        // 16 * ln(2.5) + 31 ≈ 45.66 → 45 years and ~7 months
        assertEquals(45, result.years)
        assertEquals(7, result.months)
    }

    @Test
    fun `toHumanAge is monotonically increasing across full years`() {
        val ages = (1..10).map { calculator.toHumanAge(it, 0).years }
        ages.zipWithNext { a, b -> assertTrue("age $a should be < $b", a < b) }
    }

    // ---------- toHumanAgePoint ----------

    @Test
    fun `toHumanAgePoint with 0,0 returns origin`() {
        assertEquals(AgePoint(0f, 0f), calculator.toHumanAgePoint(0, 0))
    }

    @Test
    fun `toHumanAgePoint with 1 year returns dog=1 human=31`() {
        assertEquals(AgePoint(1f, 31f), calculator.toHumanAgePoint(1, 0))
    }

    @Test
    fun `toHumanAgePoint with 0 years and 1 month returns special case 0,1`() {
        assertEquals(AgePoint(0f, 1f), calculator.toHumanAgePoint(0, 1))
    }

    @Test
    fun `toHumanAgePoint with 2 years and 6 months interpolates correctly`() {
        val point = calculator.toHumanAgePoint(2, 6)
        // (2*12 + 6) / 12 = 2.5 dog years → 16 * ln(2.5) + 31 ≈ 45.66
        assertEquals(2.5f, point.dogYears, 0.0001f)
        assertEquals(45.66f, point.humanYears, 0.05f)
    }

    // ---------- humanAgeCurve ----------

    @Test
    fun `humanAgeCurve uses default maxDogYears of 22`() {
        val data = calculator.humanAgeCurve()
        assertEquals(23, data.size)
        assertEquals(0f, data.first().dogYears, 0f)
        assertEquals(22f, data.last().dogYears, 0f)
    }

    @Test
    fun `humanAgeCurve first point is origin 0,0`() {
        val data = calculator.humanAgeCurve()
        assertEquals(AgePoint(0f, 0f), data[0])
    }

    @Test
    fun `humanAgeCurve second point matches direct dog age formula`() {
        val data = calculator.humanAgeCurve()
        // 16 * ln(1) + 31 = 31
        assertEquals(1f, data[1].dogYears, 0f)
        assertEquals(31f, data[1].humanYears, 0.001f)
    }

    @Test
    fun `humanAgeCurve honours custom maxDogYears`() {
        val data = calculator.humanAgeCurve(maxDogYears = 5)
        assertEquals(6, data.size)
        assertEquals(5f, data.last().dogYears, 0f)
    }

    @Test
    fun `humanAgeCurve human values are monotonically increasing after origin`() {
        val data = calculator.humanAgeCurve().drop(1)
        data.zipWithNext { a, b ->
            assertTrue("${a.humanYears} should be < ${b.humanYears}", a.humanYears < b.humanYears)
        }
    }
}
