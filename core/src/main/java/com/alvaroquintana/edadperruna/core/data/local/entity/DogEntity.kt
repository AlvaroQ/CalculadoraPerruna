package com.alvaroquintana.edadperruna.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey val breedId: String,
    val name: String = "",
    val life: String = "",
    val image: String = "",
    val otherNames: String = "[]",
    val shortDescription: String = "",
    // MainInformation (flattened)
    val lifeExpectancy: Int = 0,
    val lifeExpectancyMeasure: String = "",
    val character: String = "[]",
    val sizeBreed: String = "",
    val typeHair: String = "[]",
    val typeHairDescription: String = "",
    val prizeCost: String = "[]",
    val prizeCurrency: String = "",
    // FCI (flattened)
    val fciGroup: Long = 0,
    val fciGroupType: String = "",
    val fciSection: Long = 0,
    val fciSectionType: String = "",
    // PhysicalCharacteristics (flattened)
    val weightMacho: String = "[]",
    val weightHembra: String = "[]",
    val weightMedida: String = "",
    val heightMacho: String = "[]",
    val heightHembra: String = "[]",
    val heightMedida: String = "",
    val colorHair: String = "",
    val physicalDescription: String = "",
    // Other
    val commonDiseases: String = "[]",
    val hygiene: String = "",
    val lossHair: String = "",
    val nutrition: String = "",
    val lastUpdated: Long = System.currentTimeMillis(),
)
