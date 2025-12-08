package com.alvaroquintana.domain

data class MainInformation(
    var lifeExpectancy: LifeExpectancy? = null,
    var character: MutableList<String>? = mutableListOf(),
    var sizeBreed: String? = "",
    var typeHair: MutableList<String>? = mutableListOf(),
    var typeHairDescription: String? = "",
    var prize: Prize? = null,
)