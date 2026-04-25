package com.alvaroquintana.edadperruna.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.tiles.TileService
import androidx.wear.tooling.preview.devices.WearDevices
import com.alvaroquintana.edadperruna.core.domain.age.DogAgeCalculator
import com.alvaroquintana.edadperruna.core.domain.age.LogarithmicDogAgeCalculator
import com.alvaroquintana.edadperruna.wear.sync.FavoriteBreed
import com.alvaroquintana.edadperruna.wear.sync.favoriteBreedFlow
import com.alvaroquintana.edadperruna.wear.theme.PerrunoWearTheme
import com.alvaroquintana.edadperruna.wear.tile.LastCalculationStore
import com.alvaroquintana.edadperruna.wear.tile.PerrunoTileService

private val dogAgeCalculator: DogAgeCalculator = LogarithmicDogAgeCalculator()

// Three-step state machine mirrors the mobile calculator flow:
//   PickYears → PickMonths(years) → ShowResult(years, months)
// The favorite-breed shortcut skips PickMonths and jumps straight to
// ShowResult(avgLifeYears, 0) — average lifespan has no month precision.
private sealed interface PickerStep {
    data object PickYears : PickerStep
    data class PickMonths(val years: Int) : PickerStep
    data class ShowResult(val years: Int, val months: Int) : PickerStep
}

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerrunoWearTheme {
                WearApp()
            }
        }
    }
}

@Composable
private fun WearApp() {
    val context = LocalContext.current
    val favoriteBreed by remember(context) { context.favoriteBreedFlow() }
        .collectAsState(initial = null)

    var step: PickerStep by remember { mutableStateOf(PickerStep.PickYears) }

    when (val current = step) {
        is PickerStep.PickYears -> YearPicker(
            favoriteBreed = favoriteBreed,
            onYearSelected = { years -> step = PickerStep.PickMonths(years) },
            onFavoriteShortcut = { years -> step = PickerStep.ShowResult(years, 0) },
        )
        is PickerStep.PickMonths -> MonthPicker(
            years = current.years,
            onMonthSelected = { months ->
                step = PickerStep.ShowResult(current.years, months)
            },
        )
        is PickerStep.ShowResult -> {
            val humanYears = dogAgeCalculator.toHumanAge(current.years, current.months).years
            // Persist this calculation and ping the Tile so its next render
            // surfaces the real number instead of the static placeholder.
            LaunchedEffect(current.years, current.months, humanYears) {
                LastCalculationStore.save(
                    context = context,
                    dogYears = current.years,
                    dogMonths = current.months,
                    humanYears = humanYears,
                )
                TileService.getUpdater(context)
                    .requestUpdate(PerrunoTileService::class.java)
            }
            ResultView(
                dogYears = current.years,
                dogMonths = current.months,
                humanYears = humanYears,
                onReset = { step = PickerStep.PickYears },
            )
        }
    }
}

@Composable
private fun YearPicker(
    favoriteBreed: FavoriteBreed?,
    onYearSelected: (Int) -> Unit,
    onFavoriteShortcut: (Int) -> Unit,
) {
    val listState = rememberScalingLazyListState()
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 32.dp),
    ) {
        item {
            Text(
                text = "🐾 " + stringResource(R.string.wear_pick_years),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }

        favoriteBreed?.let { breed ->
            item {
                FavoriteShortcut(
                    breed = breed,
                    onClick = { onFavoriteShortcut(breed.avgLifeYears) },
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

        items(count = 20) { idx ->
            val year = idx + 1
            FilledTonalButton(
                onClick = { onYearSelected(year) },
                modifier = Modifier.padding(vertical = 2.dp),
            ) {
                Text(
                    text = "$year",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun MonthPicker(
    years: Int,
    onMonthSelected: (Int) -> Unit,
) {
    val listState = rememberScalingLazyListState()
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 32.dp),
    ) {
        item {
            Text(
                text = "🐾 " + stringResource(R.string.wear_pick_months, years),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
        items(count = 12) { month ->
            FilledTonalButton(
                onClick = { onMonthSelected(month) },
                modifier = Modifier.padding(vertical = 2.dp),
            ) {
                Text(
                    text = "$month",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun FavoriteShortcut(
    breed: FavoriteBreed,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "★ ${breed.name}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.wear_favorite_calc_button, breed.avgLifeYears),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ResultView(
    dogYears: Int,
    dogMonths: Int,
    humanYears: Int,
    onReset: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 32.dp),
        ) {
            item {
                Text(
                    text = "🐾 " + stringResource(R.string.wear_age_label, dogYears, dogMonths),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
            item {
                Spacer(Modifier.height(4.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.wear_human_years_result, humanYears),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            item {
                Spacer(Modifier.height(12.dp))
            }
            item {
                FilledTonalButton(onClick = onReset) {
                    Text("Otro")
                }
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
private fun WearAppPreview() {
    PerrunoWearTheme {
        WearApp()
    }
}
