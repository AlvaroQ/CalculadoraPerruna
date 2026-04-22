package com.alvaroquintana.edadperruna.core.data.remote

import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.google.android.gms.tasks.Tasks
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class FirestoreBreedDataSourceTest {

    private val firestore = mockk<FirebaseFirestore>()
    private val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
    private val collection = mockk<CollectionReference>()
    private val query = mockk<Query>()
    private val document = mockk<DocumentReference>()

    private val dataSource = FirestoreBreedDataSource(firestore, crashlytics)

    private fun stubListChain() {
        every { firestore.collection("breedES") } returns collection
        every { collection.orderBy("name", Query.Direction.ASCENDING) } returns query
    }

    private fun stubDocumentChain(id: String) {
        every { firestore.collection("breedES") } returns collection
        every { collection.document(id) } returns document
    }

    private fun queryDoc(dog: Dog): QueryDocumentSnapshot {
        val doc = mockk<QueryDocumentSnapshot>()
        every { doc.toObject(Dog::class.java) } returns dog
        return doc
    }

    @Test
    fun `getBreedList returns dogs mapped from the snapshot`() = runTest {
        val snapshot = mockk<QuerySnapshot>()
        every { snapshot.iterator() } returns mutableListOf(
            queryDoc(Dog(breedId = "1", name = "Labrador")),
            queryDoc(Dog(breedId = "2", name = "Poodle")),
        ).iterator()
        stubListChain()
        every { query.get() } returns Tasks.forResult(snapshot)

        val result = dataSource.getBreedList()

        assertEquals(2, result.size)
        assertEquals("Labrador", result[0].name)
        assertEquals("Poodle", result[1].name)
    }

    @Test
    fun `getBreedList queries the breedES collection ordered by name ascending`() = runTest {
        val snapshot = mockk<QuerySnapshot> {
            every { iterator() } returns mutableListOf<QueryDocumentSnapshot>().iterator()
        }
        stubListChain()
        every { query.get() } returns Tasks.forResult(snapshot)

        dataSource.getBreedList()

        verify { firestore.collection("breedES") }
        verify { collection.orderBy("name", Query.Direction.ASCENDING) }
    }

    @Test
    fun `getBreedList returns empty list and records when Firestore call fails`() = runTest {
        val error = IOException("network down")
        stubListChain()
        every { query.get() } returns Tasks.forException(error)

        val result = dataSource.getBreedList()

        assertTrue(result.isEmpty())
        verify { crashlytics.recordException(error) }
    }

    @Test
    fun `getBreedList returns empty list and records when mapping throws`() = runTest {
        val mappingError = RuntimeException("malformed document")
        val brokenDoc = mockk<QueryDocumentSnapshot> {
            every { toObject(Dog::class.java) } throws mappingError
        }
        val snapshot = mockk<QuerySnapshot> {
            every { iterator() } returns mutableListOf(brokenDoc).iterator()
        }
        stubListChain()
        every { query.get() } returns Tasks.forResult(snapshot)

        val result = dataSource.getBreedList()

        assertTrue(result.isEmpty())
        verify { crashlytics.recordException(mappingError) }
    }

    @Test
    fun `getBreedDescription returns the dog from the document`() = runTest {
        val dog = Dog(breedId = "labrador", name = "Labrador")
        val snapshot = mockk<DocumentSnapshot> {
            every { toObject(Dog::class.java) } returns dog
        }
        stubDocumentChain("labrador")
        every { document.get() } returns Tasks.forResult(snapshot)

        val result = dataSource.getBreedDescription("labrador")

        assertEquals("Labrador", result?.name)
    }

    @Test
    fun `getBreedDescription returns empty Dog when toObject returns null`() = runTest {
        val snapshot = mockk<DocumentSnapshot> {
            every { toObject(Dog::class.java) } returns null
        }
        stubDocumentChain("missing")
        every { document.get() } returns Tasks.forResult(snapshot)

        val result = dataSource.getBreedDescription("missing")

        assertNotNull(result)
        assertEquals("", result?.name)
    }

    @Test
    fun `getBreedDescription returns empty Dog and records when mapping throws`() = runTest {
        val mappingError = RuntimeException("malformed document")
        val snapshot = mockk<DocumentSnapshot> {
            every { toObject(Dog::class.java) } throws mappingError
        }
        stubDocumentChain("broken")
        every { document.get() } returns Tasks.forResult(snapshot)

        val result = dataSource.getBreedDescription("broken")

        assertNotNull(result)
        assertEquals("", result?.name)
        verify { crashlytics.recordException(mappingError) }
    }

    @Test
    fun `getBreedDescription returns null and records when Firestore call fails`() = runTest {
        val error = IOException("permission denied")
        stubDocumentChain("anything")
        every { document.get() } returns Tasks.forException(error)

        val result = dataSource.getBreedDescription("anything")

        assertNull(result)
        verify { crashlytics.recordException(error) }
    }
}
