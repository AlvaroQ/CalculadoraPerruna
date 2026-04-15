package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Prize(
    var cost: List<Int> = emptyList(),
    var currency: String = "",
)
