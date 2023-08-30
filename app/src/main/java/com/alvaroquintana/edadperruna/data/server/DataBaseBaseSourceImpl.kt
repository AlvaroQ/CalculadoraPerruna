package com.alvaroquintana.edadperruna.data.server

import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.domain.App
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.utils.Constants.PATH_REFERENCE_APPS
import com.alvaroquintana.edadperruna.utils.Constants.PATH_REFERENCE_ES_BREED
import com.alvaroquintana.edadperruna.utils.log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class DataBaseBaseSourceImpl : DataBaseSource {

    // Read from the database
    override suspend fun getBreedList(): MutableList<Dog> {
        return suspendCancellableCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_REFERENCE_ES_BREED)
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener {
                    val result: MutableList<Dog> = mutableListOf()
                    for (document in it) result.add(document.toObject(Dog::class.java))
                    continuation.resume(result.toMutableList())
                }
                .addOnFailureListener { error ->
                    // Failed to read value
                    log("DataBaseSourceImpl", "Failed to read value.", error.cause)
                    continuation.resume(mutableListOf())
                    FirebaseCrashlytics.getInstance().recordException(Throwable(error.cause))
                }
        }
    }

    override suspend fun getBreedDescription(breedId: String): Dog? {
        return suspendCancellableCoroutine {continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection(PATH_REFERENCE_ES_BREED).document(breedId)
                .get()
                .addOnSuccessListener { result ->
                    val breed = result.toObject(Dog::class.java)
                    if(breed == null) continuation.resume(Dog())
                    else continuation.resume(breed)
                }
                .addOnFailureListener { exception ->
                    log("DataBaseSourceImpl", "Failed to read value.", exception.cause)
                    continuation.resume(Dog())
                    FirebaseCrashlytics.getInstance().recordException(Throwable(exception.cause))
                }
        }
    }

    override suspend fun getAppsRecommended(): MutableList<App> {
        return suspendCancellableCoroutine { continuation ->
            FirebaseDatabase.getInstance().getReference(PATH_REFERENCE_APPS)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var value = dataSnapshot.getValue<MutableList<App>>()
                        if(value == null) value = mutableListOf()
                        continuation.resume(value.filter { it.url != BuildConfig.APPLICATION_ID }.toMutableList())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        log("DataBaseSourceImpl", "Failed to read value.", error.toException())
                        continuation.resume(mutableListOf())
                        FirebaseCrashlytics.getInstance().recordException(Throwable(error.toException()))
                    }
                })
        }
    }
}