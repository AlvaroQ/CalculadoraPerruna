package com.alvaroquintana.edadperruna.core.data.repository

import com.alvaroquintana.edadperruna.core.data.local.datastore.PreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PreferencesRepositoryImplTest {

    private val dataSource = mockk<PreferencesDataSource>(relaxUnitFun = true)

    @Test
    fun `screenViewerCount exposes the data source flow`() = runTest {
        every { dataSource.screenViewerCount } returns flowOf(7)
        every { dataSource.themeMode } returns flowOf("system")
        val repo = PreferencesRepositoryImpl(dataSource)

        val value = repo.screenViewerCount.first()

        assertEquals(7, value)
    }

    @Test
    fun `themeMode exposes the data source flow`() = runTest {
        every { dataSource.screenViewerCount } returns flowOf(0)
        every { dataSource.themeMode } returns flowOf("dark")
        val repo = PreferencesRepositoryImpl(dataSource)

        val value = repo.themeMode.first()

        assertEquals("dark", value)
    }

    @Test
    fun `incrementScreenViewer delegates to the data source`() = runTest {
        every { dataSource.screenViewerCount } returns flowOf(0)
        every { dataSource.themeMode } returns flowOf("system")
        coEvery { dataSource.incrementScreenViewer() } returns Unit
        val repo = PreferencesRepositoryImpl(dataSource)

        repo.incrementScreenViewer()

        coVerify(exactly = 1) { dataSource.incrementScreenViewer() }
    }

    @Test
    fun `setThemeMode forwards the mode value to the data source`() = runTest {
        every { dataSource.screenViewerCount } returns flowOf(0)
        every { dataSource.themeMode } returns flowOf("system")
        coEvery { dataSource.setThemeMode(any()) } returns Unit
        val repo = PreferencesRepositoryImpl(dataSource)

        repo.setThemeMode("light")

        coVerify(exactly = 1) { dataSource.setThemeMode("light") }
    }
}
