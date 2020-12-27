package com.alvaroquintana.edadperruna.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "breedId")
    val breedId: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "otherNames")
    val otherNames: MutableList<String>
)