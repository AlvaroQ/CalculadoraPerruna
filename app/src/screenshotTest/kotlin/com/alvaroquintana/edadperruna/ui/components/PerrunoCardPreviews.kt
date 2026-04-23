package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alvaroquintana.edadperruna.core.designsystem.theme.CalculadoraPerrunaTheme
import com.android.tools.screenshot.PreviewTest

@PreviewTest
@Preview(showBackground = true, name = "card_elevated")
@Composable
private fun PerrunoCardElevatedPreview() {
    CalculadoraPerrunaTheme {
        PerrunoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            variant = PerrunoCardVariant.Elevated
        ) {
            Text(
                text = "Elevated card sample",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@PreviewTest
@Preview(showBackground = true, name = "card_outlined")
@Composable
private fun PerrunoCardOutlinedPreview() {
    CalculadoraPerrunaTheme {
        PerrunoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            variant = PerrunoCardVariant.Outlined
        ) {
            Text(
                text = "Outlined card sample",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@PreviewTest
@Preview(showBackground = true, name = "card_filled")
@Composable
private fun PerrunoCardFilledPreview() {
    CalculadoraPerrunaTheme {
        PerrunoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            variant = PerrunoCardVariant.Filled
        ) {
            Text(
                text = "Filled card sample",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
