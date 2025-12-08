package com.alvaroquintana.domain

data class Prize(
    var cost: MutableList<Int> = mutableListOf(),
    var currency: String? = ""
)