package com.alvaroquintana.edadperruna.ui.result

import com.alvaroquintana.edadperruna.core.domain.age.AgePoint
import com.alvaroquintana.edadperruna.core.domain.age.DogAgeCalculator
import com.alvaroquintana.edadperruna.core.domain.age.HumanAge
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.AnalyticsEvent
import com.alvaroquintana.edadperruna.managers.Screens
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ResultViewModelTest {

    private lateinit var calculator: DogAgeCalculator
    private lateinit var analytics: Analytics
    private lateinit var viewModel: ResultViewModel

    @Before
    fun setUp() {
        calculator = mockk(relaxed = true)
        analytics = mockk(relaxed = true)
        viewModel = ResultViewModel(calculator, analytics)
    }

    @Test
    fun `translateToHuman delegates to calculator and returns its result`() {
        val expected = HumanAge(62, 1)
        every { calculator.toHumanAge(7, 0) } returns expected

        val result = viewModel.translateToHuman(7, 0)

        assertEquals(expected, result)
        verify(exactly = 1) { calculator.toHumanAge(7, 0) }
    }

    @Test
    fun `getMarkerPoint delegates to calculator and returns its result`() {
        val expected = AgePoint(2.5f, 45.66f)
        every { calculator.toHumanAgePoint(2, 6) } returns expected

        val result = viewModel.getMarkerPoint(2, 6)

        assertEquals(expected, result)
        verify(exactly = 1) { calculator.toHumanAgePoint(2, 6) }
    }

    @Test
    fun `generateChartData delegates to calculator humanAgeCurve`() {
        val expected = listOf(AgePoint(0f, 0f), AgePoint(1f, 31f), AgePoint(2f, 42.09f))
        every { calculator.humanAgeCurve() } returns expected

        val result = viewModel.generateChartData()

        assertEquals(expected, result)
        verify(exactly = 1) { calculator.humanAgeCurve() }
    }

    @Test
    fun `showAd is true by default`() {
        assertTrue(viewModel.showAd.value)
    }

    @Test
    fun `init logs RESULT screen viewed event`() {
        verify { analytics.logEvent(AnalyticsEvent.ScreenViewed(Screens.RESULT)) }
    }

    @Test
    fun `translateToHuman logs DogTranslateFinished event`() {
        every { calculator.toHumanAge(any(), any()) } returns HumanAge(0, 0)

        viewModel.translateToHuman(3, 6)

        verify { analytics.logEvent(AnalyticsEvent.DogTranslateFinished(3, 6)) }
    }
}
