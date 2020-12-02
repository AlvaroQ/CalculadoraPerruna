package com.alvaroquintana.edadperruna.data.server

import android.util.Log
import com.alvaroquintana.data.source.DataBaseSource
import com.alvaroquintana.domain.App
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.data.localfiles.FileLocalDb
import com.alvaroquintana.edadperruna.utils.Constants.PATH_REFERENCE
import com.alvaroquintana.edadperruna.utils.Constants.PATH_REFERENCE_APPS
import com.alvaroquintana.edadperruna.utils.log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class DataBaseBaseSourceImpl(private val localDb: FileLocalDb) : DataBaseSource {
    private val esCollectionPath = "descriptionES"

    // Read from the database
    override suspend fun getBreedList(): MutableList<Dog> {
        return suspendCancellableCoroutine { continuation ->
            FirebaseDatabase.getInstance().getReference(PATH_REFERENCE)
                .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var value = dataSnapshot.getValue<MutableList<Dog>>()
                    if(value == null) value = mutableListOf()
                    continuation.resume(value.toMutableList())
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

    override suspend fun getBreedDescription(breedId: String): Dog? {
        val collectionRoute = esCollectionPath
        return suspendCancellableCoroutine {continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection(collectionRoute)
                .get()
                .addOnSuccessListener { result ->
                    val breed = result.find { it.data["breedId"] == breedId  }?.toObject(Dog::class.java)
                    continuation.resume(breed!!)
                }
                .addOnFailureListener { exception ->
                    continuation.resume(null)
                }
        }
    }

    override suspend fun updateBreeeds() {
        val TAG = "UPLOAD_BREEDS"
        val gson = Gson()
        val breedsES = localDb.loadJSONFromAsset("breed_description")?:""
        val listES = gson.fromJson<List<Dog>>(breedsES)
        val db = FirebaseFirestore.getInstance()
        for (breed in listES){
            db.collection(esCollectionPath).document(breed.breedId!!)
                .set(breed)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }
    private inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)
}