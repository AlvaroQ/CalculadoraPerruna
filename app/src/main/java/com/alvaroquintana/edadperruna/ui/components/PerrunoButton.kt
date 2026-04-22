package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvaroquintana.edadperruna.ui.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.ui.theme.PerrunoTheme
import com.alvaroquintana.edadperruna.ui.theme.PerrunoTokens

enum class PerrunoButtonSize(val minHeight: Dp, val iconSize: Dp, val fontSize: Int, val horizontalPadding: Dp) {
    Small(minHeight = 40.dp, iconSize = 18.dp, fontSize = 14, horizontalPadding = 16.dp),
    Medium(minHeight = 48.dp, iconSize = 20.dp, fontSize = 14, horizontalPadding = 24.dp),
    Large(minHeight = 56.dp, iconSize = 22.dp, fontSize = 16, horizontalPadding = 24.dp)
}

@Composable
fun PerrunoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    size: PerrunoButtonSize = PerrunoButtonSize.Large
) {
    val perrunoColors = PerrunoTheme.colors
    val primaryColor = perrunoColors.buttonGradientStart
    val primaryDark = perrunoColors.buttonGradientEnd
    val disabledColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val onPrimary = perrunoColors.buttonContent
    val onSurfaceDisabled = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(primaryColor, primaryDark)
    )

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f
        ),
        label = "buttonScale"
    )

    val contentColor = if (enabled) onPrimary else onSurfaceDisabled

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = PerrunoShapes.full,
                ambientColor = primaryColor.copy(alpha = 0.25f),
                spotColor = primaryColor.copy(alpha = 0.25f)
            )
            .defaultMinSize(minHeight = size.minHeight)
            .clip(PerrunoShapes.full)
            .background(
                brush = if (enabled) gradientBrush
                else Brush.verticalGradient(listOf(disabledColor, disabledColor))
            )
            .then(
                if (enabled && !isLoading) {
                    Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    isPressed = true
                                    tryAwaitRelease()
                                    isPressed = false
                                }
                            )
                        }
                        .clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(
                horizontal = size.horizontalPadding,
                vertical = PerrunoTokens.Spacing.md
            )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(PerrunoTokens.Spacing.xl),
                color = onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(size.iconSize)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = size.fontSize.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    ),
                    color = contentColor
                )
            }
        }
    }
}
