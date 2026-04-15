package com.alvaroquintana.edadperruna.ui.home

import com.alvaroquintana.edadperruna.core.domain.model.Dog
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
}
