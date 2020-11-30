package com.alvaroquintana.edadperruna.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "icon")
    val icon: String,

    @ColumnInfo(name = "name")
    val name: String
)