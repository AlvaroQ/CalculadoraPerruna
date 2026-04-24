package com.alvaroquintana.edadperruna.core.data.repository

import com.alvaroquintana.edadperruna.core.data.local.datastore.PreferencesDataSource
import com.alvaroquintana.edadperruna.core.domain.model.FavoriteBreed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PreferencesRepositoryImplTest {

    private val dataSource = mockk<PreferencesDataSource>(relaxUnitFun = true)

    @Before
    fun stubReadOnlyFlows() {
        every { dataSource.screenViewerCount } returns flowOf(0)
        every { dataSource.themeMode } returns flowOf("system")
        every { dataSource.favoriteBreed } returns flowOf(null)
    }

    @Test
    fun `screenViewerCount exposes the data source flow`() = runTest {
        every { dataSource.screenViewerCount } returns flowOf(7)
        val repo = PreferencesRepositoryImpl(dataSource)

        val value = repo.screenViewerCount.first()

        assertEquals(7, value)
    }

    @Test
    fun `themeMode exposes the data source flow`() = runTest {
        every { dataSource.themeMode } returns flowOf("dark")
        val repo = PreferencesRepositoryImpl(dataSource)

        val value = repo.themeMode.first()

        assertEquals("dark", value)
    }

    @Test
    fun `favoriteBreed exposes the data source flow`() = runTest {
        val payload = FavoriteBreed("husky-id", "Husky", "https://img/husky", 13)
        every { dataSource.favoriteBreed } returns flowOf(payload)
        val repo = PreferencesRepositoryImpl(dataSource)

        val value = repo.favoriteBreed.first()

        assertEquals(payload, value)
    }

    @Test
    fun `favoriteBreed emits null when none is stored`() = runTest {
        val repo = PreferencesRepositoryImpl(dataSource)

        assertNull(repo.favoriteBreed.first())
    }

    @Test
    fun `incrementScreenViewer delegates to the data source`() = runTest {
        coEvery { dataSource.incrementScreenViewer() } returns Unit
        val repo = PreferencesRepositoryImpl(dataSource)

        repo.incrementScreenViewer()

        coVerify(exactly = 1) { dataSource.incrementScreenViewer() }
    }

    @Test
    fun `setThemeMode forwards the mode value to the data source`() = runTest {
        coEvery { dataSource.setThemeMode(any()) } returns Unit
        val repo = PreferencesRepositoryImpl(dataSource)

        repo.setThemeMode("light")

        coVerify(exactly = 1) { dataSource.setThemeMode("light") }
    }

    @Test
    fun `setFavoriteBreed forwards the payload to the data source`() = runTest {
        val payload = FavoriteBreed("husky-id", "Husky", "https://img/husky", 13)
        coEvery { dataSource.setFavoriteBreed(any()) } returns Unit
        val repo = PreferencesRepositoryImpl(dataSource)

        repo.setFavoriteBreed(payload)

        coVerify(exactly = 1) { dataSource.setFavoriteBreed(payload) }
    }

    @Test
    fun `clearFavoriteBreed delegates to the data source`() = runTest {
        coEvery { dataSource.clearFavoriteBreed() } returns Unit
        val repo = PreferencesRepositoryImpl(dataSource)

        repo.clearFavoriteBreed()

        coVerify(exactly = 1) { dataSource.clearFavoriteBreed() }
    }
}
