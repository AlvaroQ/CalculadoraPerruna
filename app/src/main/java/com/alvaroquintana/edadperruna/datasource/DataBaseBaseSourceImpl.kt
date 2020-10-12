package com.alvaroquintana.edadperruna.datasource

import android.util.Log
import com.alvaroquintana.data.datasource.DataBaseSource
import com.alvaroquintana.domain.Dog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class DataBaseBaseSourceImpl : DataBaseSource {

    // Read from the database
    override suspend fun getBreedList(): MutableList<Dog> {
        return suspendCancellableCoroutine { continuation ->
            FirebaseDatabase.getInstance().getReference("dog/breeds")
                .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var value = dataSnapshot.getValue<MutableList<Dog>>()
                    if(value == null) value = mutableListOf()
                    continuation.resume(value.toMutableList())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("DataBaseBaseSourceImpl", "Failed to read value.", error.toException())
                    continuation.resume(mutableListOf())
                }
            })
        }
    }
/*
    override suspend fun getBreedList(): List<Dog> {
        return suspendCancellableCoroutine { continuation ->
            val collection = database.collection(COLLECTION_ADVERTS)
            collection.get()
                .addOnSuccessListener {
                    continuation.resume(it.toObjects())
                }
                .addOnFailureListener {
                    continuation.resume(emptyList())
                }
        }
    }
*/

    companion object {
        const val COLLECTION_ADVERTS = "dogs"
    }
}