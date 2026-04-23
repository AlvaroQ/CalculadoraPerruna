package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTheme

@Composable
fun GradientOverlay(
    modifier: Modifier = Modifier,
    startColor: Color = PerrunoTheme.colors.gradientOverlayStart,
    endColor: Color = PerrunoTheme.colors.gradientOverlayEnd
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(startColor, endColor)
                )
            )
    )
}
