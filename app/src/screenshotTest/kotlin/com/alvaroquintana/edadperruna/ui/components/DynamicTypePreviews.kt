package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alvaroquintana.edadperruna.core.designsystem.theme.CalculadoraPerrunaTheme
import com.android.tools.screenshot.PreviewTest

// ===========================================================================
// Dynamic type stress tests — fontScale 2.0
// Ensures WCAG 2.2 AA target of supporting text scaling to 200% without
// truncation, overflow, or clipping of interactive targets.
// ===========================================================================

@PreviewTest
@Preview(showBackground = true, name = "button_fontScale_200", fontScale = 2.0f)
@Composable
private fun PerrunoButtonFontScale200Preview() {
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
@Preview(showBackground = true, name = "card_fontScale_200", fontScale = 2.0f)
@Composable
private fun PerrunoCardFontScale200Preview() {
    CalculadoraPerrunaTheme {
        PerrunoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            variant = PerrunoCardVariant.Elevated
        ) {
            Text(
                text = "Cuerpo de tarjeta con texto largo para verificar wrapping en fontScale 2.0",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@PreviewTest
@Preview(showBackground = true, name = "infochip_fontScale_200", fontScale = 2.0f)
@Composable
private fun InfoChipFontScale200Preview() {
    CalculadoraPerrunaTheme {
        InfoChip(
            icon = Icons.Rounded.Schedule,
            value = "12 anos",
            label = "Esperanza de vida",
            modifier = Modifier.padding(16.dp)
        )
    }
}
