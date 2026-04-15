package com.alvaroquintana.edadperruna.managers

import com.alvaroquintana.edadperruna.core.domain.repository.PreferencesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdManagerTest {

    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)

    private fun createAdManager(screenViewerCount: Int): AdManager {
        every { preferencesRepository.screenViewerCount } returns flowOf(screenViewerCount)
        return AdManager(preferencesRepository)
    }

    @Test
    fun `shouldShowAd returns false when count is 0`() = runTest {
        val adManager = createAdManager(0)
        assertFalse(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns false when count is 1`() = runTest {
        val adManager = createAdManager(1)
        assertFalse(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns false when count is 2`() = runTest {
        val adManager = createAdManager(2)
        assertFalse(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns false when count is 3`() = runTest {
        val adManager = createAdManager(3)
        assertFalse(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns true when count is 4`() = runTest {
        val adManager = createAdManager(4)
        assertTrue(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns true when count is 8`() = runTest {
        val adManager = createAdManager(8)
        assertTrue(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns true when count is 12`() = runTest {
        val adManager = createAdManager(12)
        assertTrue(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd returns false when count is 5`() = runTest {
        val adManager = createAdManager(5)
        assertFalse(adManager.shouldShowAd())
    }

    @Test
    fun `shouldShowAd increments screen viewer count`() = runTest {
        val adManager = createAdManager(0)
        adManager.shouldShowAd()
        coVerify { preferencesRepository.incrementScreenViewer() }
    }
}
