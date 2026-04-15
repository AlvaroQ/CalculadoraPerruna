package com.alvaroquintana.edadperruna.ui.settings

import app.cash.turbine.test
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val themeModeFlow = MutableStateFlow("system")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { preferencesRepository.themeMode } returns themeModeFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `themeMode defaults to system`() = runTest {
        val viewModel = SettingsViewModel(preferencesRepository)

        viewModel.themeMode.test {
            assertEquals("system", awaitItem())
        }
    }

    @Test
    fun `themeMode reflects repository changes`() = runTest {
        val viewModel = SettingsViewModel(preferencesRepository)

        viewModel.themeMode.test {
            assertEquals("system", awaitItem())
            themeModeFlow.value = "dark"
            assertEquals("dark", awaitItem())
        }
    }

    @Test
    fun `setThemeMode calls repository`() = runTest {
        val viewModel = SettingsViewModel(preferencesRepository)

        viewModel.setThemeMode("light")

        coVerify { preferencesRepository.setThemeMode("light") }
    }
}
