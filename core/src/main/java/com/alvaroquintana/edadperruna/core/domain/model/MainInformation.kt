package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MainInformation(
    var lifeExpectancy: LifeExpectancy? = null,
    var character: List<String> = emptyList(),
    var sizeBreed: String = "",
    var typeHair: List<String> = emptyList(),
    var typeHairDescription: String = "",
    var prize: Prize? = null,
)
