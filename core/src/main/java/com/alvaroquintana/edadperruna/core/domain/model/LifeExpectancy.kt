package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LifeExpectancy(
    var expectancy: Int = 0,
    var measure: String = "",
)
