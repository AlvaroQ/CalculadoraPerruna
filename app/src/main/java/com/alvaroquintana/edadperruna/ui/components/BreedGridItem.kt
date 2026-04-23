package com.alvaroquintana.edadperruna.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens

@Composable
fun BreedGridItem(
    name: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(0.75f)
            .shadow(
                elevation = PerrunoTokens.Elevation.xs,
                shape = PerrunoShapes.md
            ),
        shape = PerrunoShapes.md,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = PerrunoTokens.Elevation.xs
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Full-size breed image
            PerrunoAsyncImage(
                imageUrl = imageUrl,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                shape = PerrunoShapes.md
            )

            // Gradient overlay for text readability
            GradientOverlay(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(fraction = 0.45f)
                    .align(Alignment.BottomCenter),
                startColor = Color.Transparent,
                endColor = Color.Black.copy(alpha = 0.72f)
            )

            // Breed name overlay
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                ),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = PerrunoTokens.Spacing.sm,
                        end = PerrunoTokens.Spacing.sm,
                        bottom = PerrunoTokens.Spacing.sm
                    )
            )
        }
    }
}
