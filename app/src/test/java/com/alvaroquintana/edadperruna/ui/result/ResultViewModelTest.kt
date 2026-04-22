package com.alvaroquintana.edadperruna.ui.result

import org.junit.Assert.assertEquals
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
}
