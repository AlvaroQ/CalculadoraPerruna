package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Dog(
    var breedId: String = "",
    var name: String = "",
    var life: String = "",
    var image: String = "",
    var otherNames: List<String> = emptyList(),
    var shortDescription: String = "",
    var mainInformation: MainInformation? = null,
    var fci: FCI? = null,
    var physicalCharacteristics: PhysicalCharacteristics? = null,
    var commonDiseases: List<String> = emptyList(),
    var hygiene: String = "",
    var lossHair: String = "",
    var nutrition: String = "",
)
