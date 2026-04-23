package com.alvaroquintana.edadperruna.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.GlanceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.cornerRadius
import com.alvaroquintana.edadperruna.core.designsystem.theme.DarkColorScheme
import com.alvaroquintana.edadperruna.core.designsystem.theme.LightColorScheme

/**
 * Home-screen widget for CalculadoraPerruna.
 *
 * First iteration (PR #7): ships a static "launcher"-style widget that shows the app name,
 * a paw glyph, and opens the app on tap. Persisting favorite breed + human-age snapshot
 * requires exposing a DataStore-backed use case from `:core` (follow-up PR).
 *
 * SizeMode.Exact lets us react to user-resized widgets; for now the layout is identical
 * across sizes, only padding scales.
 */
class FavoriteBreedWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(
                colors = ColorProviders(light = LightColorScheme, dark = DarkColorScheme)
            ) {
                FavoriteBreedContent()
            }
        }
    }
}

@Composable
private fun FavoriteBreedContent() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.primaryContainer)
            .cornerRadius(16.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Edad Perruna",
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            text = "Toca para calcular",
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 12.sp,
            )
        )
    }
}
