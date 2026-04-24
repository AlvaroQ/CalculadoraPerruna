package com.alvaroquintana.edadperruna.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens

sealed interface PerrunoCardVariant {
    data object Elevated : PerrunoCardVariant
    data object Outlined : PerrunoCardVariant
    data object Filled : PerrunoCardVariant
}

@Composable
fun PerrunoCard(
    modifier: Modifier = Modifier,
    variant: PerrunoCardVariant = PerrunoCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    when (variant) {
        is PerrunoCardVariant.Elevated -> {
            if (onClick != null) {
                ElevatedCard(
                    onClick = onClick,
                    modifier = modifier,
                    shape = PerrunoShapes.md,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = PerrunoTokens.Elevation.sm
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PerrunoTokens.Spacing.lg),
                        content = content
                    )
                }
            } else {
                ElevatedCard(
                    modifier = modifier,
                    shape = PerrunoShapes.md,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = PerrunoTokens.Elevation.sm
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PerrunoTokens.Spacing.lg),
                        content = content
                    )
                }
            }
        }

        is PerrunoCardVariant.Outlined -> {
            if (onClick != null) {
                OutlinedCard(
                    onClick = onClick,
                    modifier = modifier,
                    shape = PerrunoShapes.md,
                    border = BorderStroke(
                        width = PerrunoTokens.Elevation.xs,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    elevation = CardDefaults.outlinedCardElevation(
                        defaultElevation = PerrunoTokens.Elevation.none
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PerrunoTokens.Spacing.lg),
                        content = content
                    )
                }
            } else {
                OutlinedCard(
                    modifier = modifier,
                    shape = PerrunoShapes.md,
                    border = BorderStroke(
                        width = PerrunoTokens.Elevation.xs,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    elevation = CardDefaults.outlinedCardElevation(
                        defaultElevation = PerrunoTokens.Elevation.none
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PerrunoTokens.Spacing.lg),
                        content = content
                    )
                }
            }
        }

        is PerrunoCardVariant.Filled -> {
            if (onClick != null) {
                Card(
                    onClick = onClick,
                    modifier = modifier,
                    shape = PerrunoShapes.md,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = PerrunoTokens.Elevation.none
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PerrunoTokens.Spacing.lg),
                        content = content
                    )
                }
            } else {
                Card(
                    modifier = modifier,
                    shape = PerrunoShapes.md,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = PerrunoTokens.Elevation.none
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PerrunoTokens.Spacing.lg),
                        content = content
                    )
                }
            }
        }
    }
}
