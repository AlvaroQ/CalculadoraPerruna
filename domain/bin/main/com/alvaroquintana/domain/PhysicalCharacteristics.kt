package com.alvaroquintana.domain

data class PhysicalCharacteristics(
    var weight: Weight? = null,
    var height: Height? = null,
    var colorHair: String? = "",
    var description: String? = ""
)