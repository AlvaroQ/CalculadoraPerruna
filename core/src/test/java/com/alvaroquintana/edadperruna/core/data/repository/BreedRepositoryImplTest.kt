package com.alvaroquintana.edadperruna.core.data.repository

import com.alvaroquintana.edadperruna.core.data.local.dao.DogDao
import com.alvaroquintana.edadperruna.core.data.local.entity.DogEntity
import com.alvaroquintana.edadperruna.core.data.remote.FirestoreBreedDataSource
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalCoroutinesApi::class)
class BreedRepositoryImplTest {

    private val firestoreDataSource = mockk<FirestoreBreedDataSource>()
    private val dogDao = mockk<DogDao>(relaxed = true)

    private val repo = BreedRepositoryImpl(firestoreDataSource, dogDao)

    @Test
    fun `getBreedList fetches from remote when local is empty`() = runTest {
        val remoteDogs = listOf(Dog(breedId = "1", name = "Labrador"))
        coEvery { dogDao.count() } returns 0
        coEvery { firestoreDataSource.getBreedList() } returns remoteDogs
        coEvery { dogDao.getAllDogs() } returns flowOf(emptyList())

        repo.getBreedList().first()

        coVerify { firestoreDataSource.getBreedList() }
        coVerify { dogDao.insertAll(any()) }
    }

    @Test
    fun `getBreedList returns local data when cache exists`() = runTest {
        val entity = DogEntity(breedId = "1", name = "Labrador")
        coEvery { dogDao.count() } returns 1
        coEvery { dogDao.getAllDogs() } returns flowOf(listOf(entity))

        val result = repo.getBreedList().first()

        assertEquals(1, result.size)
        assertEquals("Labrador", result[0].name)
        coVerify(exactly = 0) { firestoreDataSource.getBreedList() }
    }

    @Test
    fun `refreshBreeds deletes and reinserts`() = runTest {
        val remoteDogs = listOf(Dog(breedId = "1", name = "Poodle"))
        coEvery { firestoreDataSource.getBreedList() } returns remoteDogs

        repo.refreshBreeds()

        coVerify { dogDao.deleteAll() }
        coVerify { dogDao.insertAll(any()) }
    }

    @Test
    fun `concurrent getBreedList collectors trigger only one remote fetch`() =
        runTest(UnconfinedTestDispatcher()) {
            val count = AtomicInteger(0)
            val remoteDogs = listOf(Dog(breedId = "1", name = "Labrador"))

            coEvery { dogDao.count() } answers { count.get() }
            coEvery { dogDao.getAllDogs() } returns flowOf(emptyList())
            coEvery { firestoreDataSource.getBreedList() } coAnswers {
                // Force a suspension point so the second collector can race
                // the populate check if the mutex was not protecting it.
                yield()
                remoteDogs
            }
            coEvery { dogDao.insertAll(any()) } coAnswers { count.set(remoteDogs.size) }

            val a = async { repo.getBreedList().first() }
            val b = async { repo.getBreedList().first() }
            a.await()
            b.await()

            coVerify(exactly = 1) { firestoreDataSource.getBreedList() }
            coVerify(exactly = 1) { dogDao.insertAll(any()) }
            coVerify(exactly = 1) { dogDao.deleteAll() }
        }
}
