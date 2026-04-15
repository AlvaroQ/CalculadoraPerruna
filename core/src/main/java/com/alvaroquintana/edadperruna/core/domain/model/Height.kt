package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Height(
    var macho: List<Int> = emptyList(),
    var hembra: List<Int> = emptyList(),
    var medida: String = "",
)
