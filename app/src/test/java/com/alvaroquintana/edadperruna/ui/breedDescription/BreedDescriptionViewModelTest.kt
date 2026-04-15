package com.alvaroquintana.edadperruna.ui.breedDescription

import app.cash.turbine.test
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import com.alvaroquintana.edadperruna.managers.AdManager
import com.alvaroquintana.edadperruna.ui.common.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BreedDescriptionViewModelTest {

    private val breedRepository = mockk<BreedRepository>()
    private val adManager = mockk<AdManager>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = BreedDescriptionViewModel(breedRepository, adManager)

    @Test
    fun `initial state is Loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)
        }
    }

    @Test
    fun `loadBreed emits Success with dog data`() = runTest {
        val dog = Dog(breedId = "1", name = "Labrador", shortDescription = "Friendly dog")
        coEvery { breedRepository.getBreedDescription("1") } returns flowOf(dog)
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        viewModel.loadBreed("1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals("Labrador", (state as UiState.Success).data?.name)
        }
    }

    @Test
    fun `loadBreed emits Success with null when breed not found`() = runTest {
        coEvery { breedRepository.getBreedDescription("999") } returns flowOf(null)
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        viewModel.loadBreed("999")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(null, (state as UiState.Success).data)
        }
    }

    @Test
    fun `loadBreed emits Error when repository fails`() = runTest {
        coEvery { breedRepository.getBreedDescription("1") } returns kotlinx.coroutines.flow.flow {
            throw RuntimeException("Firestore error")
        }
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        viewModel.loadBreed("1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals("Firestore error", (state as UiState.Error).message)
        }
    }

    @Test
    fun `loadBreed updates showAd from adManager`() = runTest {
        coEvery { breedRepository.getBreedDescription("1") } returns flowOf(Dog(breedId = "1"))
        coEvery { adManager.shouldShowAd() } returns true

        val viewModel = createViewModel()
        viewModel.loadBreed("1")

        viewModel.showAd.test {
            assertEquals(true, awaitItem())
        }
    }
}
