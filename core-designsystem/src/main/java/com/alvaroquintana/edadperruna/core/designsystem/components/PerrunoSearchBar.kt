package com.alvaroquintana.edadperruna.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.alvaroquintana.edadperruna.core.designsystem.R
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens

@Composable
fun PerrunoSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onSortToggle: () -> Unit,
    isSortDescending: Boolean,
    onGridToggle: () -> Unit,
    spanCount: Int,
    modifier: Modifier = Modifier
) {
    val sortRotation by animateFloatAsState(
        targetValue = if (isSortDescending) 180f else 0f,
        animationSpec = tween(durationMillis = PerrunoTokens.Motion.MEDIUM_MS),
        label = "sortRotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = PerrunoTokens.Spacing.lg,
                vertical = PerrunoTokens.Spacing.sm
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        Box(
            modifier = Modifier
                .size(PerrunoTokens.Spacing.xxxl + PerrunoTokens.Spacing.lg)
                .clip(PerrunoShapes.full)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.width(PerrunoTokens.Spacing.sm))

        // Search text field
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {}),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(PerrunoShapes.full)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(
                            horizontal = PerrunoTokens.Spacing.lg,
                            vertical = PerrunoTokens.Spacing.md
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.cd_search_breed),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") },
                            modifier = Modifier.size(PerrunoTokens.Spacing.xl)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.cd_clear_search),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(PerrunoTokens.Spacing.lg)
                            )
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.width(PerrunoTokens.Spacing.xs))

        // Sort button with rotation animation
        IconButton(onClick = onSortToggle) {
            Icon(
                imageVector = Icons.Rounded.SortByAlpha,
                contentDescription = if (isSortDescending) stringResource(R.string.cd_sort_ascending) else stringResource(R.string.cd_sort_descending),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.graphicsLayer { rotationZ = sortRotation }
            )
        }

        // Grid toggle button
        IconButton(onClick = onGridToggle) {
            Icon(
                imageVector = if (spanCount > 1) Icons.Rounded.GridView else Icons.AutoMirrored.Rounded.List,
                contentDescription = if (spanCount > 1) stringResource(R.string.cd_switch_to_list) else stringResource(R.string.cd_switch_to_grid),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
