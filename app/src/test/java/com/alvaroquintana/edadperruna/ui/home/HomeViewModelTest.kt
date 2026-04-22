package com.alvaroquintana.edadperruna.ui.home

import app.cash.turbine.test
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    private val viewModel = HomeViewModel()

    private fun dogWithName(name: String = "Labrador") = Dog(name = name)

    @Test
    fun `checkErrors returns false when breed is empty`() {
        val result = viewModel.checkErrors(Dog(), "1", "0")
        assertFalse(result)
    }

    @Test
    fun `checkErrors returns false when year is empty`() {
        val result = viewModel.checkErrors(dogWithName(), "", "0")
        assertFalse(result)
    }

    @Test
    fun `checkErrors returns false when month is empty`() {
        val result = viewModel.checkErrors(dogWithName(), "1", "")
        assertFalse(result)
    }

    @Test
    fun `checkErrors returns false when month is greater than 11`() {
        val result = viewModel.checkErrors(dogWithName(), "1", "12")
        assertFalse(result)
    }

    @Test
    fun `checkErrors returns false when month is negative`() {
        val result = viewModel.checkErrors(dogWithName(), "1", "-1")
        assertFalse(result)
    }

    @Test
    fun `checkErrors returns true with valid inputs`() {
        val result = viewModel.checkErrors(dogWithName(), "3", "6")
        assertTrue(result)
    }

    @Test
    fun `checkErrors returns true with 0 months`() {
        val result = viewModel.checkErrors(dogWithName(), "1", "0")
        assertTrue(result)
    }

    @Test
    fun `checkErrors returns true with 11 months`() {
        val result = viewModel.checkErrors(dogWithName(), "1", "11")
        assertTrue(result)
    }

    @Test
    fun `navigateToBreedList emits NavigateToBreedList event`() = runTest {
        viewModel.events.test {
            viewModel.navigateToBreedList()
            assertEquals(HomeViewModel.HomeEvent.NavigateToBreedList, awaitItem())
        }
    }

    @Test
    fun `navigateToResult emits NavigateToResult event with the dog`() = runTest {
        val dog = dogWithName("Beagle")

        viewModel.events.test {
            viewModel.navigateToResult(dog)
            val event = awaitItem()
            assertTrue(event is HomeViewModel.HomeEvent.NavigateToResult)
            assertEquals("Beagle", (event as HomeViewModel.HomeEvent.NavigateToResult).breed.name)
        }
    }

    @Test
    fun `checkErrors emits ShowError with BreedEmpty when breed is empty`() = runTest {
        viewModel.events.test {
            viewModel.checkErrors(Dog(), "1", "0")
            val event = awaitItem()
            assertTrue(event is HomeViewModel.HomeEvent.ShowError)
            assertEquals(HomeViewModel.HomeError.BreedEmpty, (event as HomeViewModel.HomeEvent.ShowError).error)
        }
    }

    @Test
    fun `checkErrors emits MonthIllegal for non-numeric month`() = runTest {
        viewModel.events.test {
            viewModel.checkErrors(dogWithName(), "1", "abc")
            val event = awaitItem()
            assertTrue(event is HomeViewModel.HomeEvent.ShowError)
            assertEquals(HomeViewModel.HomeError.MonthIllegal, (event as HomeViewModel.HomeEvent.ShowError).error)
        }
    }
}
