package com.alvaroquintana.edadperruna.wear.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

/**
 * Wear-side theming for the Edad Perruna brand.
 *
 * Why this exists as a separate composable from the mobile `CalculadoraPerrunaTheme`:
 * `MaterialTheme` and `ColorScheme` on Wear come from `androidx.wear.compose.material3`,
 * which is a distinct artifact from the mobile `androidx.compose.material3`. The
 * constructors aren't source-compatible (Wear adds `*Dim` variants, drops
 * `surfaceVariant` / `inverse*` / `scrim`, etc.), so the theme must be expressed
 * twice — same hex values, different API shape.
 *
 * Keeping this inside `:wear` instead of `:core-designsystem` avoids pulling
 * the mobile Compose Material3 stack (+Coil + material-icons-extended + the
 * 12 DynaPuff TTFs) onto the watch, which would regress cold-start and APK size.
 *
 * Typography is left at Wear's default on purpose — custom display fonts
 * (DynaPuff) hurt legibility on a 192-384dp round screen. Brand identity on
 * the watch comes from color + shape, not type.
 */
@Composable
fun PerrunoWearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) perrunoDarkColorScheme() else perrunoLightColorScheme(),
        content = content,
    )
}

private fun perrunoLightColorScheme(): ColorScheme = ColorScheme(
    primary = WearLightPrimary,
    primaryDim = WearLightPrimaryDim,
    primaryContainer = WearLightPrimaryContainer,
    onPrimary = WearLightOnPrimary,
    onPrimaryContainer = WearLightOnPrimaryContainer,
    secondary = WearLightSecondary,
    secondaryDim = WearLightSecondaryDim,
    secondaryContainer = WearLightSecondaryContainer,
    onSecondary = WearLightOnSecondary,
    onSecondaryContainer = WearLightOnSecondaryContainer,
    tertiary = WearLightTertiary,
    tertiaryDim = WearLightTertiaryDim,
    tertiaryContainer = WearLightTertiaryContainer,
    onTertiary = WearLightOnTertiary,
    onTertiaryContainer = WearLightOnTertiaryContainer,
    surfaceContainerLow = WearLightSurfaceContainerLow,
    surfaceContainer = WearLightSurfaceContainer,
    surfaceContainerHigh = WearLightSurfaceContainerHigh,
    onSurface = WearLightOnSurface,
    onSurfaceVariant = WearLightOnSurfaceVariant,
    outline = WearLightOutline,
    outlineVariant = WearLightOutlineVariant,
    background = WearLightBackground,
    onBackground = WearLightOnBackground,
    error = WearLightError,
    errorDim = WearLightErrorDim,
    errorContainer = WearLightErrorContainer,
    onError = WearLightOnError,
    onErrorContainer = WearLightOnErrorContainer,
)

private fun perrunoDarkColorScheme(): ColorScheme = ColorScheme(
    primary = WearDarkPrimary,
    primaryDim = WearDarkPrimaryDim,
    primaryContainer = WearDarkPrimaryContainer,
    onPrimary = WearDarkOnPrimary,
    onPrimaryContainer = WearDarkOnPrimaryContainer,
    secondary = WearDarkSecondary,
    secondaryDim = WearDarkSecondaryDim,
    secondaryContainer = WearDarkSecondaryContainer,
    onSecondary = WearDarkOnSecondary,
    onSecondaryContainer = WearDarkOnSecondaryContainer,
    tertiary = WearDarkTertiary,
    tertiaryDim = WearDarkTertiaryDim,
    tertiaryContainer = WearDarkTertiaryContainer,
    onTertiary = WearDarkOnTertiary,
    onTertiaryContainer = WearDarkOnTertiaryContainer,
    surfaceContainerLow = WearDarkSurfaceContainerLow,
    surfaceContainer = WearDarkSurfaceContainer,
    surfaceContainerHigh = WearDarkSurfaceContainerHigh,
    onSurface = WearDarkOnSurface,
    onSurfaceVariant = WearDarkOnSurfaceVariant,
    outline = WearDarkOutline,
    outlineVariant = WearDarkOutlineVariant,
    background = WearDarkBackground,
    onBackground = WearDarkOnBackground,
    error = WearDarkError,
    errorDim = WearDarkErrorDim,
    errorContainer = WearDarkErrorContainer,
    onError = WearDarkOnError,
    onErrorContainer = WearDarkOnErrorContainer,
)
