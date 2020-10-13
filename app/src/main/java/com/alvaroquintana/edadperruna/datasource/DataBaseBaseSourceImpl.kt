package com.alvaroquintana.edadperruna.datasource

import android.util.Log
import com.alvaroquintana.data.datasource.DataBaseSource
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.utils.log
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
            FirebaseDatabase.getInstance().getReference(PATH_REFERENCE)
                .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var value = dataSnapshot.getValue<MutableList<Dog>>()
                    if(value == null) value = mutableListOf()
                    continuation.resume(value.toMutableList())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    log("DataBaseBaseSourceImpl", "Failed to read value.", error.toException())
                    continuation.resume(mutableListOf())
                }
            })
        }
    }

    companion object {
        const val PATH_REFERENCE = "dog/breeds"
    }
}