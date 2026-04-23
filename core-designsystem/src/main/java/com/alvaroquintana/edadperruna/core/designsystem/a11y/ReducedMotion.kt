package com.alvaroquintana.edadperruna.core.designsystem.a11y

import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Reads the system-wide "Remove animations" accessibility setting.
 *
 * Apps that respect this flag degrade spring/tween animations to a near-zero duration
 * (or to a simple fade) to avoid triggering vestibular or focus issues for users who
 * opted into reduced motion at the OS level.
 *
 * The value is read once per composition (settings changes require recomposition —
 * acceptable since this is rare and the recomposition is cheap).
 */
@Composable
fun rememberIsReducedMotionEnabled(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        isReducedMotionEnabled(context)
    }
}

private fun isReducedMotionEnabled(context: Context): Boolean {
    // Settings.Global.ANIMATOR_DURATION_SCALE = 0.0f when the user disabled animations.
    val scale = Settings.Global.getFloat(
        context.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    )
    return scale == 0f
}
