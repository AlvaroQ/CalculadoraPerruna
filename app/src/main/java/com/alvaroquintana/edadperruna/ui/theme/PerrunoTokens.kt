package com.alvaroquintana.edadperruna.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.ui.unit.dp

object PerrunoTokens {

    object Layout {
        val maxContentWidth = 600.dp
    }

    object Spacing {
        val xxs = 2.dp
        val xs = 4.dp
        val sm = 8.dp
        val md = 12.dp
        val lg = 16.dp
        val xl = 20.dp
        val xxl = 24.dp
        val xxxl = 32.dp
        val huge = 48.dp
    }

    object Radius {
        val xs = 4.dp
        val sm = 8.dp
        val md = 12.dp
        val lg = 16.dp
        val xl = 28.dp
        val full = 999.dp
    }

    object Elevation {
        val none = 0.dp
        val xs = 1.dp
        val sm = 2.dp
        val md = 4.dp
        val lg = 8.dp
    }

    object Motion {
        const val SHORT_MS = 150
        const val MEDIUM_MS = 300
        const val LONG_MS = 500
        const val STAGGER_MS = 100
        val EaseInOut = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    }
}
