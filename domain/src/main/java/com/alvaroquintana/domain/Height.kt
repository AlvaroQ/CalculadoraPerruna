package com.alvaroquintana.domain

data class Height(
    var macho: MutableList<Int> = mutableListOf(),
    var hembra: MutableList<Int> = mutableListOf(),
    var medida: String? = ""
)