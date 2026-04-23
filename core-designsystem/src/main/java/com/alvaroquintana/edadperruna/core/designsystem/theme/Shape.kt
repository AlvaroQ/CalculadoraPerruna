package com.alvaroquintana.edadperruna.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object PerrunoShapes {
    val none = RoundedCornerShape(0.dp)
    val xs = RoundedCornerShape(PerrunoTokens.Radius.xs)
    val sm = RoundedCornerShape(PerrunoTokens.Radius.sm)
    val md = RoundedCornerShape(PerrunoTokens.Radius.md)
    val lg = RoundedCornerShape(PerrunoTokens.Radius.lg)
    val xl = RoundedCornerShape(PerrunoTokens.Radius.xl)
    val full = RoundedCornerShape(PerrunoTokens.Radius.full)

    /** Card shape — medium rounding */
    val card = md

    /** Button shape — pill-like */
    val button = xl

    /** Bottom sheet / dialog */
    val bottomSheet = RoundedCornerShape(
        topStart = PerrunoTokens.Radius.xl,
        topEnd = PerrunoTokens.Radius.xl,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    /** Chip / badge */
    val chip = full
}
