package com.alvaroquintana.domain

data class Dog(
        var breedId: String? = "",
        var name: String? = "",
        var life: String? = "",
        var image: String? = "",
        var otherNames: MutableList<String>? = mutableListOf(),
        var shortDescription: String? = "",
        var mainInformation: MainInformation? = null,
        var fci: FCI? = null,
        var physicalCharacteristics: PhysicalCharacteristics? = null,
        var commonDiseases: MutableList<String>? = mutableListOf(),
        var hygiene: String? = "",
        var lossHair: String? = "",
        var nutrition: String? = ""
)