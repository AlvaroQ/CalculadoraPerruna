package com.alvaroquintana.edadperruna.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvaroquintana.edadperruna.BuildConfig
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.utils.rateApp
import com.alvaroquintana.edadperruna.utils.shareApp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alvaroquintana.edadperruna.ui.components.AdaptiveContainer
import com.alvaroquintana.edadperruna.ui.components.PerrunoTopBar
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentTheme by viewModel.themeMode.collectAsStateWithLifecycle()
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PerrunoTopBar(
                title = stringResource(R.string.settings),
                onBack = onBackClick
            )
        }
    ) { paddingValues ->
        AdaptiveContainer(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Appearance Section
            SettingsCategoryHeader(title = stringResource(R.string.settings_appearance))

            SettingsItem(
                icon = R.drawable.ic_theme,
                title = stringResource(R.string.settings_dark_mode),
                subtitle = getThemeDisplayName(currentTheme),
                onClick = { showThemeDialog = true }
            )

            // About Section
            SettingsCategoryHeader(title = stringResource(R.string.settings_about))

            SettingsItem(
                icon = R.drawable.ic_star,
                title = stringResource(R.string.settings_rate_app),
                subtitle = stringResource(R.string.settings_rate_app_summary),
                onClick = { rateApp(context) }
            )

            SettingsItem(
                icon = R.drawable.ic_share,
                title = stringResource(R.string.settings_share),
                subtitle = stringResource(R.string.settings_share_summary),
                onClick = { shareApp(-1, context) }
            )

            SettingsItem(
                icon = R.drawable.ic_version,
                title = stringResource(R.string.settings_version),
                subtitle = "${stringResource(R.string.settings_version)} ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
                onClick = { }
            )
        }
        }

        if (showThemeDialog) {
            ThemeSelectionDialog(
                currentTheme = currentTheme,
                onThemeSelected = { theme ->
                    viewModel.setThemeMode(theme)
                    applyTheme(theme)
                    showThemeDialog = false
                },
                onDismiss = { showThemeDialog = false }
            )
        }
    }
}

@Composable
private fun SettingsCategoryHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(
            start = PerrunoTokens.Spacing.lg,
            top = PerrunoTokens.Spacing.xxl,
            bottom = PerrunoTokens.Spacing.sm
        )
    )
}

@Composable
private fun SettingsItem(
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = PerrunoTokens.Spacing.lg, vertical = PerrunoTokens.Spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with badge background
        Box(
            modifier = Modifier
                .size(PerrunoTokens.Spacing.xxxl + PerrunoTokens.Spacing.sm)
                .clip(PerrunoShapes.sm)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(PerrunoTokens.Spacing.xl)
            )
        }
        Spacer(modifier = Modifier.width(PerrunoTokens.Spacing.lg))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.xxs))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val themeOptions = listOf(
        "light" to stringResource(R.string.day_mode),
        "dark" to stringResource(R.string.night_mode),
        "system" to stringResource(R.string.system_mode)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_dark_mode)) },
        text = {
            Column {
                themeOptions.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(value) }
                            .padding(vertical = PerrunoTokens.Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == value,
                            onClick = { onThemeSelected(value) }
                        )
                        Spacer(modifier = Modifier.width(PerrunoTokens.Spacing.sm))
                        Text(text = label)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        shape = PerrunoShapes.lg
    )
}

@Composable
private fun getThemeDisplayName(theme: String): String {
    return when (theme) {
        "light" -> stringResource(R.string.day_mode)
        "dark" -> stringResource(R.string.night_mode)
        else -> stringResource(R.string.system_mode)
    }
}

private fun applyTheme(themeValue: String) {
    val mode = when (themeValue) {
        "light" -> AppCompatDelegate.MODE_NIGHT_NO
        "dark" -> AppCompatDelegate.MODE_NIGHT_YES
        "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(mode)
}
