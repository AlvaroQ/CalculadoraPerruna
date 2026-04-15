package com.alvaroquintana.edadperruna.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alvaroquintana.edadperruna.core.data.local.entity.DogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {

    @Query("SELECT * FROM dogs ORDER BY name ASC")
    fun getAllDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs WHERE breedId = :breedId")
    fun getDogById(breedId: String): Flow<DogEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dogs: List<DogEntity>)

    @Query("DELETE FROM dogs")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM dogs")
    suspend fun count(): Int
}
