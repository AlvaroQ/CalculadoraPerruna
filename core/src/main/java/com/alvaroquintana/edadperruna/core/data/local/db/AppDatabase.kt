package com.alvaroquintana.edadperruna.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alvaroquintana.edadperruna.core.data.local.dao.DogDao
import com.alvaroquintana.edadperruna.core.data.local.entity.DogEntity

@Database(entities = [DogEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
}
