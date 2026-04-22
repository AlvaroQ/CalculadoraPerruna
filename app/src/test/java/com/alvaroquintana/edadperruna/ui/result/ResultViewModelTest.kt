package com.alvaroquintana.edadperruna.ui.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultViewModelTest {

    private val viewModel = ResultViewModel()

    @Test
    fun `translateToHuman with 0 years and 0 months returns 0`() {
        val result = viewModel.translateToHuman(0, 0)
        assertEquals(0, result.years)
        assertEquals(0, result.months)
    }

    @Test
    fun `translateToHuman with 0 years and 1 month returns 1 year`() {
        val result = viewModel.translateToHuman(0, 1)
        assertEquals(1, result.years)
    }

    @Test
    fun `translateToHuman with 1 year and 0 months returns 31`() {
        val result = viewModel.translateToHuman(1, 0)
        assertEquals(31, result.years)
    }

    @Test
    fun `translateToHuman with 7 years returns approximately 62`() {
        val result = viewModel.translateToHuman(7, 0)
        // 16 * ln(7) + 31 ≈ 62.12
        assertEquals(62, result.years)
    }

    @Test
    fun `generateChartData has 23 points from 0 to 22`() {
        val data = viewModel.generateChartData()
        assertEquals(23, data.size)
        assertEquals(0f, data.first().dogYears)
        assertEquals(22f, data.last().dogYears)
    }

    @Test
    fun `generateChartData first point is 0,0`() {
        val data = viewModel.generateChartData()
        assertEquals(0f, data[0].dogYears)
        assertEquals(0f, data[0].humanYears)
    }

    @Test
    fun `getMarkerPoint with 0,0 returns origin`() {
        val point = viewModel.getMarkerPoint(0, 0)
        assertEquals(0f, point.dogYears)
        assertEquals(0f, point.humanYears)
    }

    @Test
    fun `getMarkerPoint with 1 year returns dog=1, human=31`() {
        val point = viewModel.getMarkerPoint(1, 0)
        assertEquals(1f, point.dogYears)
        assertEquals(31f, point.humanYears)
    }

    @Test
    fun `getMarkerPoint with 0 years and 1 month returns 0,1 special case`() {
        val point = viewModel.getMarkerPoint(0, 1)
        assertEquals(0f, point.dogYears)
        assertEquals(1f, point.humanYears)
    }

    @Test
    fun `getMarkerPoint with 2 years and 6 months interpolates correctly`() {
        val point = viewModel.getMarkerPoint(2, 6)
        // (2*12 + 6) / 12 = 2.5 dog years
        assertEquals(2.5f, point.dogYears)
        // 16 * ln(2.5) + 31 ≈ 45.66
        assertEquals(45.66f, point.humanYears, 0.05f)
    }

    @Test
    fun `translateToHuman with 2 years and 6 months computes human months`() {
        val result = viewModel.translateToHuman(2, 6)
        // 16 * ln(2.5) + 31 ≈ 45.66 → 45 years and ~7 months
        assertEquals(45, result.years)
        assertEquals(7, result.months)
    }

    @Test
    fun `translateToHuman is monotonically increasing across full years`() {
        val ages = (1..10).map { viewModel.translateToHuman(it, 0).years }
        ages.zipWithNext { a, b -> assertTrue("age $a should be < $b", a < b) }
    }

    @Test
    fun `generateChartData second point matches direct dog age formula`() {
        val data = viewModel.generateChartData()
        // 16 * ln(1) + 31 = 31
        assertEquals(1f, data[1].dogYears)
        assertEquals(31f, data[1].humanYears, 0.001f)
    }
}
