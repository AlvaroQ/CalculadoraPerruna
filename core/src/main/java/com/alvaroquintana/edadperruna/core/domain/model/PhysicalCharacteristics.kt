package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PhysicalCharacteristics(
    var weight: Weight? = null,
    var height: Height? = null,
    var colorHair: String = "",
    var description: String = "",
)
