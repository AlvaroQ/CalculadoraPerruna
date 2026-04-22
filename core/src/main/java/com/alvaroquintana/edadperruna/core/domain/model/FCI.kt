package com.alvaroquintana.edadperruna.core.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class FCI(
    var group: Long = 0,
    var groupType: String = "",
    var section: Long = 0,
    var sectionType: String = "",
)
