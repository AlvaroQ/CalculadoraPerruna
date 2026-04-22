package com.alvaroquintana.edadperruna.core.data.remote

import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FirestoreBreedDataSource @Inject constructor() {

    suspend fun getBreedList(): List<Dog> = suspendCancellableCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_BREED_ES)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val result = try {
                    snapshot.map { doc -> doc.toObject(Dog::class.java) }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    emptyList()
                }
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }
            .addOnFailureListener { error ->
                if (continuation.isActive) {
                    continuation.resume(emptyList())
                }
                FirebaseCrashlytics.getInstance().recordException(error)
            }
    }

    suspend fun getBreedDescription(breedId: String): Dog? = suspendCancellableCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_BREED_ES)
            .document(breedId)
            .get()
            .addOnSuccessListener { result ->
                val dog = try {
                    result.toObject(Dog::class.java) ?: Dog()
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    Dog()
                }
                if (continuation.isActive) {
                    continuation.resume(dog)
                }
            }
            .addOnFailureListener { error ->
                if (continuation.isActive) {
                    continuation.resume(null)
                }
                FirebaseCrashlytics.getInstance().recordException(error)
            }
    }

    private companion object {
        const val COLLECTION_BREED_ES = "breedES"
    }
}
