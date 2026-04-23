package com.alvaroquintana.edadperruna.core.designsystem.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
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

    /**
     * Expressive motion specs — local substitute for Material 3 Expressive's MotionScheme.expressive()
     * until the official API becomes public (internal in compose-material3 BOM 2026.03.01).
     *
     * Intent:
     *  - "Expressive" = playful, slightly overshooting springs for hero elements (result, favourite selection).
     *  - "Standard" = productive motion for list entries, shimmer, navigation transitions.
     */
    object ExpressiveMotion {
        /** Hero number / result reveal. Overshoots and settles — emotional. */
        fun <T> heroSpring(): SpringSpec<T> = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )

        /** Card / chip entry. Slight bounce, quick settle. */
        fun <T> entrySpring(): SpringSpec<T> = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )

        /** Button press / micro-interaction. Snappy, no bounce. */
        fun <T> pressSpring(): SpringSpec<T> = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
    }
}
