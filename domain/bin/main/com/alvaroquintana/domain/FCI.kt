package com.alvaroquintana.domain

data class FCI(
    var group: Long = 0,
    var groupType: String? = "",
    var section: Long = 0,
    var sectionType: String? = ""
)