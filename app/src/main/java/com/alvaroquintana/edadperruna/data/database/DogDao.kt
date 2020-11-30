package com.alvaroquintana.edadperruna.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DogDao {

    @Query("SELECT * FROM Dogs")
    fun getAll(): MutableList<Dog>

    @Query("SELECT COUNT(id) FROM Dogs")
    fun dogCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDogs(dogs: List<Dog>)
}