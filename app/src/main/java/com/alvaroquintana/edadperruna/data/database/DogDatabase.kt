package com.alvaroquintana.edadperruna.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Dog::class], version = 1, exportSchema = false)
abstract class DogDatabase : RoomDatabase() {

    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            DogDatabase::class.java,
            "dog-db"
        ).build()
    }

    abstract fun dogDao(): DogDao
}