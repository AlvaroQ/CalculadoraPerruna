package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alvaroquintana.edadperruna.core.designsystem.theme.CalculadoraPerrunaTheme
import com.android.tools.screenshot.PreviewTest

@PreviewTest
@Preview(showBackground = true, name = "infochip_size")
@Composable
private fun InfoChipSizePreview() {
    CalculadoraPerrunaTheme {
        InfoChip(
            icon = Icons.Rounded.Pets,
            value = "Medio",
            label = "Tamano",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@PreviewTest
@Preview(showBackground = true, name = "infochip_life")
@Composable
private fun InfoChipLifePreview() {
    CalculadoraPerrunaTheme {
        InfoChip(
            icon = Icons.Rounded.Schedule,
            value = "12 anos",
            label = "Esperanza de vida",
            modifier = Modifier.padding(16.dp)
        )
    }
}
