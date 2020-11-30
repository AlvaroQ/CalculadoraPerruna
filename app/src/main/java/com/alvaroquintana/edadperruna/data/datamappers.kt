package com.alvaroquintana.edadperruna.data

import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.data.database.Dog as DomainDog

fun Dog.toRoomDog(): DomainDog =
    DomainDog(
        0,
        icon!!,
        name!!
    )

fun DomainDog.toDomainDog(): Dog =
    Dog(
        icon = icon,
        name = name
    )
