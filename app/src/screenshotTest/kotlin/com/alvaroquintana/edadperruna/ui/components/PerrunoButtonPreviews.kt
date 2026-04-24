package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoButton
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoButtonSize
import com.alvaroquintana.edadperruna.core.designsystem.theme.CalculadoraPerrunaTheme
import com.android.tools.screenshot.PreviewTest

@PreviewTest
@Preview(showBackground = true, name = "button_large_enabled")
@Composable
private fun PerrunoButtonLargeEnabledPreview() {
    CalculadoraPerrunaTheme {
        PerrunoButton(
            text = "Calcular",
            onClick = {},
            icon = Icons.Rounded.Calculate,
            size = PerrunoButtonSize.Large
        )
    }
}

@PreviewTest
@Preview(showBackground = true, name = "button_large_disabled")
@Composable
private fun PerrunoButtonLargeDisabledPreview() {
    CalculadoraPerrunaTheme {
        PerrunoButton(
            text = "Calcular",
            onClick = {},
            icon = Icons.Rounded.Calculate,
            enabled = false,
            size = PerrunoButtonSize.Large
        )
    }
}

@PreviewTest
@Preview(showBackground = true, name = "button_large_loading")
@Composable
private fun PerrunoButtonLargeLoadingPreview() {
    CalculadoraPerrunaTheme {
        PerrunoButton(
            text = "Calcular",
            onClick = {},
            isLoading = true,
            size = PerrunoButtonSize.Large
        )
    }
}

@PreviewTest
@Preview(showBackground = true, name = "button_medium_enabled")
@Composable
private fun PerrunoButtonMediumEnabledPreview() {
    CalculadoraPerrunaTheme {
        PerrunoButton(
            text = "Calcular",
            onClick = {},
            icon = Icons.Rounded.Calculate,
            size = PerrunoButtonSize.Medium
        )
    }
}

@PreviewTest
@Preview(showBackground = true, name = "button_small_enabled")
@Composable
private fun PerrunoButtonSmallEnabledPreview() {
    CalculadoraPerrunaTheme {
        PerrunoButton(
            text = "Calcular",
            onClick = {},
            icon = Icons.Rounded.Calculate,
            size = PerrunoButtonSize.Small
        )
    }
}
