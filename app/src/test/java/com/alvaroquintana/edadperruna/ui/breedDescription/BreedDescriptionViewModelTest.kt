package com.alvaroquintana.edadperruna.ui.breedDescription

import app.cash.turbine.test
import com.alvaroquintana.edadperruna.core.data.network.ConnectivityObserver
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.model.FavoriteBreed
import com.alvaroquintana.edadperruna.core.domain.repository.BreedRepository
import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import com.alvaroquintana.edadperruna.managers.AdManager
import com.alvaroquintana.edadperruna.managers.Analytics
import com.alvaroquintana.edadperruna.managers.AnalyticsEvent
import com.alvaroquintana.edadperruna.managers.Screens
import com.alvaroquintana.edadperruna.ui.common.UiState
import com.alvaroquintana.edadperruna.wearsync.FavoriteBreedPayload
import com.alvaroquintana.edadperruna.wearsync.WearSyncPublisher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val preferencesRepository = mockk<PreferencesRepository>(relaxUnitFun = true)
    private val wearSyncPublisher = mockk<WearSyncPublisher>(relaxUnitFun = true)
    private val adManager = mockk<AdManager>()
    private val connectivityObserver = mockk<ConnectivityObserver> {
        every { isOnline } returns true
    }
    private val analytics = mockk<Analytics>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val favoriteFlow = MutableStateFlow<FavoriteBreed?>(null)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { preferencesRepository.favoriteBreed } returns favoriteFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        BreedDescriptionViewModel(
            breedRepository,
            preferencesRepository,
            wearSyncPublisher,
            adManager,
            connectivityObserver,
            analytics,
        )

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

    @Test
    fun `loadBreed emits showNoInternet when device is offline`() = runTest {
        every { connectivityObserver.isOnline } returns false
        coEvery { breedRepository.getBreedDescription("1") } returns flowOf(Dog(breedId = "1"))
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        viewModel.loadBreed("1")

        viewModel.showNoInternet.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `loadBreed keeps showNoInternet false when device is online`() = runTest {
        coEvery { breedRepository.getBreedDescription("1") } returns flowOf(Dog(breedId = "1"))
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        viewModel.loadBreed("1")

        viewModel.showNoInternet.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `dismissNoInternet resets the flag back to false`() = runTest {
        every { connectivityObserver.isOnline } returns false
        coEvery { breedRepository.getBreedDescription("1") } returns flowOf(Dog(breedId = "1"))
        coEvery { adManager.shouldShowAd() } returns false

        val viewModel = createViewModel()
        viewModel.loadBreed("1")

        viewModel.showNoInternet.test {
            assertEquals(true, awaitItem())
            viewModel.dismissNoInternet()
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `init logs BREED_DESCRIPTION screen viewed event`() = runTest {
        createViewModel()

        verify { analytics.logEvent(AnalyticsEvent.ScreenViewed(Screens.BREED_DESCRIPTION)) }
    }

    @Test
    fun `toggleFavorite persists the breed and pushes it to the watch when not favorited`() = runTest {
        val viewModel = createViewModel()

        viewModel.toggleFavorite("husky-id", "Husky", "https://img/husky", 13)

        coVerify {
            preferencesRepository.setFavoriteBreed(
                FavoriteBreed("husky-id", "Husky", "https://img/husky", 13),
            )
        }
        coVerify {
            wearSyncPublisher.publishFavoriteBreed(
                FavoriteBreedPayload("Husky", "https://img/husky", 13),
            )
        }
    }

    @Test
    fun `toggleFavorite clears the favorite when the same breed is toggled again`() = runTest {
        favoriteFlow.value = FavoriteBreed("husky-id", "Husky", "https://img/husky", 13)
        val viewModel = createViewModel()

        viewModel.toggleFavorite("husky-id", "Husky", "https://img/husky", 13)

        coVerify(exactly = 1) { preferencesRepository.clearFavoriteBreed() }
        coVerify(exactly = 0) { wearSyncPublisher.publishFavoriteBreed(any()) }
    }

    @Test
    fun `toggleFavorite clamps avgLifeYears to a minimum of 1`() = runTest {
        val viewModel = createViewModel()

        viewModel.toggleFavorite("husky-id", "Husky", "https://img/husky", 0)

        coVerify {
            preferencesRepository.setFavoriteBreed(
                FavoriteBreed("husky-id", "Husky", "https://img/husky", 1),
            )
        }
    }
}
