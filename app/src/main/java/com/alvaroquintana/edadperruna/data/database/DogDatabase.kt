package com.alvaroquintana.edadperruna.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Dog::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DogDatabase : RoomDatabase() {

    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            DogDatabase::class.java,
            "dog-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigrationFrom(1, 2)
            .build()
    }

    abstract fun dogDao(): DogDao
}