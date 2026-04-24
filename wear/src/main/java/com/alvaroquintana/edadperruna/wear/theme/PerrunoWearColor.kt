package com.alvaroquintana.edadperruna.wear.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// Golden Paws — Wear-side palette
//
// Same hex values as :core-designsystem/theme/Color.kt, duplicated here on
// purpose: :wear depends on :core-domain-pure only, so it can't drag in the
// whole :core-designsystem module (which brings Compose Material3 mobile,
// Coil, material-icons-extended, Glance-compatible typography and the 12
// DynaPuff TTFs). Keeping a tiny ~20-line color mirror here is cheaper than
// either a shared-colors module or bloating Wear cold-start.
// ============================================================

// Light
internal val WearLightPrimary = Color(0xFFB8860B)
internal val WearLightOnPrimary = Color(0xFFFFFFFF)
internal val WearLightPrimaryContainer = Color(0xFFFFE0A0)
internal val WearLightOnPrimaryContainer = Color(0xFF3A2500)
internal val WearLightPrimaryDim = Color(0xFFFFD180)

internal val WearLightSecondary = Color(0xFF6F5B40)
internal val WearLightOnSecondary = Color(0xFFFFFFFF)
internal val WearLightSecondaryContainer = Color(0xFFFADEBB)
internal val WearLightOnSecondaryContainer = Color(0xFF271904)
internal val WearLightSecondaryDim = Color(0xFFDEC3A2)

internal val WearLightTertiary = Color(0xFF51664A)
internal val WearLightOnTertiary = Color(0xFFFFFFFF)
internal val WearLightTertiaryContainer = Color(0xFFD3ECC8)
internal val WearLightOnTertiaryContainer = Color(0xFF0F210C)
internal val WearLightTertiaryDim = Color(0xFFB8CFAD)

internal val WearLightBackground = Color(0xFFFFF8F0)
internal val WearLightOnBackground = Color(0xFF1F1B13)
internal val WearLightOnSurface = Color(0xFF1F1B13)
internal val WearLightOnSurfaceVariant = Color(0xFF50453A)
internal val WearLightOutline = Color(0xFF82735F)
internal val WearLightOutlineVariant = Color(0xFFD4C5B0)

internal val WearLightSurfaceContainerLow = Color(0xFFFFF3E2)
internal val WearLightSurfaceContainer = Color(0xFFFAEDD8)
internal val WearLightSurfaceContainerHigh = Color(0xFFF4E7D2)

internal val WearLightError = Color(0xFFBA1A1A)
internal val WearLightOnError = Color(0xFFFFFFFF)
internal val WearLightErrorContainer = Color(0xFFFFDAD6)
internal val WearLightOnErrorContainer = Color(0xFF410002)
internal val WearLightErrorDim = Color(0xFFFFB4AB)

// Dark — the one Wear OS actually ships by default (AMOLED-friendly)
internal val WearDarkPrimary = Color(0xFFFFD180)
internal val WearDarkOnPrimary = Color(0xFF5E3C00)
internal val WearDarkPrimaryContainer = Color(0xFF875500)
internal val WearDarkOnPrimaryContainer = Color(0xFFFFDEA8)
internal val WearDarkPrimaryDim = Color(0xFFB8860B)

internal val WearDarkSecondary = Color(0xFFDEC3A2)
internal val WearDarkOnSecondary = Color(0xFF3E2D16)
internal val WearDarkSecondaryContainer = Color(0xFF56432A)
internal val WearDarkOnSecondaryContainer = Color(0xFFFADEBB)
internal val WearDarkSecondaryDim = Color(0xFF6F5B40)

internal val WearDarkTertiary = Color(0xFFB8CFAD)
internal val WearDarkOnTertiary = Color(0xFF24371F)
internal val WearDarkTertiaryContainer = Color(0xFF3A4E34)
internal val WearDarkOnTertiaryContainer = Color(0xFFD3ECC8)
internal val WearDarkTertiaryDim = Color(0xFF51664A)

// True black background — essential for AMOLED pixel-off / battery
internal val WearDarkBackground = Color(0xFF000000)
internal val WearDarkOnBackground = Color(0xFFEEE1CC)
internal val WearDarkOnSurface = Color(0xFFEEE1CC)
internal val WearDarkOnSurfaceVariant = Color(0xFFD4C5B0)
internal val WearDarkOutline = Color(0xFF9D8E79)
internal val WearDarkOutlineVariant = Color(0xFF50453A)

internal val WearDarkSurfaceContainerLow = Color(0xFF1F1B13)
internal val WearDarkSurfaceContainer = Color(0xFF241F17)
internal val WearDarkSurfaceContainerHigh = Color(0xFF2E2921)

internal val WearDarkError = Color(0xFFFFB4AB)
internal val WearDarkOnError = Color(0xFF690005)
internal val WearDarkErrorContainer = Color(0xFF93000A)
internal val WearDarkOnErrorContainer = Color(0xFFFFDAD6)
internal val WearDarkErrorDim = Color(0xFFBA1A1A)
