package com.alvaroquintana.edadperruna.core.data.remote

import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreBreedDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics,
) {

    suspend fun getBreedList(): List<Dog> = try {
        firestore.collection(COLLECTION_BREED_ES)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .await()
            .map { doc -> doc.toObject(Dog::class.java) }
    } catch (e: Exception) {
        crashlytics.recordException(e)
        emptyList()
    }

    // Distinguishes two failure modes on purpose:
    //  - Firestore call fails -> null (caller treats as "not loaded")
    //  - Document exists but mapping fails -> Dog() (caller treats as "loaded but empty")
    suspend fun getBreedDescription(breedId: String): Dog? {
        val snapshot = try {
            firestore.collection(COLLECTION_BREED_ES)
                .document(breedId)
                .get()
                .await()
        } catch (e: Exception) {
            crashlytics.recordException(e)
            return null
        }
        return try {
            snapshot.toObject(Dog::class.java) ?: Dog()
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Dog()
        }
    }

    private companion object {
        const val COLLECTION_BREED_ES = "breedES"
    }
}
