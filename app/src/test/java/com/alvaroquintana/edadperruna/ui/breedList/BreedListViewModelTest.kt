package com.alvaroquintana.edadperruna.ui.breedList

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
class BreedListViewModelTest {

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

    private fun createViewModel() = BreedListViewModel(breedRepository, adManager)

    @Test
    fun `init loads breed list and emits Success`() = runTest {
        val dogs = listOf(Dog(breedId = "1", name = "Labrador"), Dog(breedId = "2", name = "Poodle"))
        coEvery { breedRepository.getBreedList() } returns flowOf(dogs)
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(2, (state as UiState.Success).data.size)
            assertEquals("Labrador", state.data[0].name)
        }
    }

    @Test
    fun `init emits Error when repository fails`() = runTest {
        coEvery { breedRepository.getBreedList() } returns kotlinx.coroutines.flow.flow {
            throw RuntimeException("Network error")
        }
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals("Network error", (state as UiState.Error).message)
        }
    }

    @Test
    fun `showAd reflects adManager result`() = runTest {
        coEvery { breedRepository.getBreedList() } returns flowOf(emptyList())
        coEvery { adManager.shouldShowAd() } returns true

        val viewModel = createViewModel()

        viewModel.showAd.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `onDogClicked emits NavigateToDescription event`() = runTest {
        coEvery { breedRepository.getBreedList() } returns flowOf(emptyList())
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        val dog = Dog(breedId = "1", name = "Labrador")

        viewModel.events.test {
            viewModel.onDogClicked(dog)
            val event = awaitItem()
            assertTrue(event is BreedListViewModel.BreedListEvent.NavigateToDescription)
            assertEquals("Labrador", (event as BreedListViewModel.BreedListEvent.NavigateToDescription).dog.name)
        }
    }

    @Test
    fun `onDogLongClicked emits ExpandImage event`() = runTest {
        coEvery { breedRepository.getBreedList() } returns flowOf(emptyList())
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onDogLongClicked("https://example.com/image.jpg")
            val event = awaitItem()
            assertTrue(event is BreedListViewModel.BreedListEvent.ExpandImage)
            assertEquals("https://example.com/image.jpg", (event as BreedListViewModel.BreedListEvent.ExpandImage).imageUrl)
        }
    }
}
