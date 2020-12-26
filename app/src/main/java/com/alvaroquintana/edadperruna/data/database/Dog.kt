package com.alvaroquintana.edadperruna.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alvaroquintana.domain.FCI
import com.alvaroquintana.domain.MainInformation
import com.alvaroquintana.domain.PhysicalCharacteristics

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "breedId")
    val breedId: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "name")
    val name: String
)