package com.alvaroquintana.edadperruna.ui.result

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.components.AdaptiveContainer
import com.alvaroquintana.edadperruna.ui.components.GradientOverlay
import com.alvaroquintana.edadperruna.ui.components.PerrunoAsyncImage
import com.alvaroquintana.edadperruna.ui.components.PerrunoButton
import com.alvaroquintana.edadperruna.ui.components.PerrunoCard
import com.alvaroquintana.edadperruna.ui.components.PerrunoCardVariant
import com.alvaroquintana.edadperruna.ui.components.PerrunoTopBar
import com.alvaroquintana.edadperruna.core.designsystem.a11y.rememberIsReducedMotionEnabled
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTheme
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    years: Int,
    months: Int,
    image: String,
    name: String,
    life: String,
    onBackClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onImageClick: (String) -> Unit = {},
    viewModel: ResultViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val showAd by viewModel.showAd.collectAsStateWithLifecycle()

    val humanAge = remember { viewModel.translateToHuman(years, months) }
    val chartData = remember { viewModel.generateChartData() }

    // Hero reveal — expressive spring on the human-age number.
    // Respects the "Remove animations" a11y setting (snaps to target immediately).
    val reducedMotion = rememberIsReducedMotionEnabled()
    var heroVisible by remember { mutableStateOf(false) }
    val heroScale by animateFloatAsState(
        targetValue = if (heroVisible) 1f else 0.6f,
        animationSpec = if (reducedMotion) snap() else PerrunoTokens.ExpressiveMotion.heroSpring(),
        label = "heroScale"
    )
    val heroAlpha by animateFloatAsState(
        targetValue = if (heroVisible) 1f else 0f,
        animationSpec = if (reducedMotion) snap() else PerrunoTokens.ExpressiveMotion.entrySpring(),
        label = "heroAlpha"
    )
    LaunchedEffect(Unit) { heroVisible = true }

    // Predictive back — the hero number shrinks/fades in sync with the swipe gesture progress.
    // Falls back to the default back animation on pre-Android 14 devices where progress is absent.
    PredictiveBackHandler(enabled = true) { progress ->
        try {
            progress.collect { backEvent ->
                heroVisible = backEvent.progress < 0.1f
            }
            onBackClick()
        } catch (_: kotlinx.coroutines.CancellationException) {
            // Gesture cancelled — restore hero
            heroVisible = true
        }
    }

    // Vico chart model producer
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(chartData) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    x = chartData.map { it.dogYears.toDouble() },
                    y = chartData.map { it.humanYears.toDouble() }
                )
            }
        }
    }

    val chartLineColor = PerrunoTheme.colors.chartLine

    Scaffold(
        topBar = {
            PerrunoTopBar(
                title = stringResource(R.string.completed),
                onBack = onBackClick
            )
        }
    ) { paddingValues ->
        AdaptiveContainer(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(PerrunoTokens.Spacing.lg)
            ) {
                // Time Header Card
                PerrunoCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = PerrunoCardVariant.Filled
                ) {
                    Text(
                        text = formatTimeText(years, months),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                // Breed Image Card — zero-padding so image fills edge-to-edge
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clickable { onImageClick(image) },
                    shape = com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes.md,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = PerrunoTokens.Elevation.none
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        PerrunoAsyncImage(
                            imageUrl = image,
                            contentDescription = name,
                            modifier = Modifier.fillMaxSize(),
                            shape = com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes.md
                        )

                        GradientOverlay(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .align(Alignment.BottomCenter),
                            startColor = androidx.compose.ui.graphics.Color.Transparent,
                            endColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f)
                        )

                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleLarge,
                            color = androidx.compose.ui.graphics.Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(PerrunoTokens.Spacing.lg)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                // Result Card — liveRegion makes TalkBack announce the human-age result on reveal
                PerrunoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true) {
                            liveRegion = LiveRegionMode.Polite
                        },
                    variant = PerrunoCardVariant.Elevated
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.equal_to),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.sm))

                            Text(
                                text = formatHumanAge(humanAge.years, humanAge.months),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = heroScale
                                        scaleY = heroScale
                                        alpha = heroAlpha
                                    }
                                    .semantics { heading() }
                            )

                            Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.xs))

                            Text(
                                text = stringResource(R.string.of_people),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.nbcnews.com/health/health-news/how-old-your-dog-new-equation-shows-how-calculate-its-n1233459")
                                    )
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                                contentDescription = stringResource(R.string.expand),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                // Chart Card — Vico line chart
                PerrunoCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = PerrunoCardVariant.Filled
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.human_years),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.xs))

                        CartesianChartHost(
                            chart = rememberCartesianChart(
                                rememberLineCartesianLayer(
                                    lineProvider = LineCartesianLayer.LineProvider.series(
                                        LineCartesianLayer.rememberLine(
                                            fill = LineCartesianLayer.LineFill.single(
                                                Fill(chartLineColor)
                                            )
                                        )
                                    )
                                ),
                                startAxis = VerticalAxis.rememberStart(),
                                bottomAxis = HorizontalAxis.rememberBottom(),
                            ),
                            modelProducer = modelProducer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.xs))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = stringResource(R.string.juvenil),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.adult),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.senior),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.xs))

                        Text(
                            text = stringResource(R.string.dog_years),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                // Info Card
                PerrunoCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = PerrunoCardVariant.Filled
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val breedSteps = stringArrayResource(R.array.breed_step)
                        val categoryIndex = when {
                            (years * 12 + months < 4) -> 0
                            (years * 12 + months < 13) -> 1
                            (years * 12 + months < 84) -> 2
                            else -> 3
                        }

                        Text(
                            text = breedSteps[categoryIndex],
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                        HorizontalDivider(
                            modifier = Modifier.width(60.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                        Text(
                            text = stringResource(R.string.life_resumen, name, life),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.md))

                        Text(
                            text = stringResource(R.string.save_puppy),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                // Try Again Button
                PerrunoButton(
                    text = stringResource(R.string.try_again),
                    onClick = onTryAgainClick,
                    icon = ImageVector.vectorResource(R.drawable.ic_refresh),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun formatTimeText(years: Int, months: Int): String {
    return when {
        years == 0 -> {
            val m = pluralStringResource(R.plurals.month, months, months)
            stringResource(R.string.time_month, m)
        }
        months == 0 -> {
            val y = pluralStringResource(R.plurals.year, years, years)
            stringResource(R.string.time_year, y)
        }
        else -> {
            val m = pluralStringResource(R.plurals.month, months, months)
            val y = pluralStringResource(R.plurals.year, years, years)
            stringResource(R.string.time_year_month, y, m)
        }
    }
}

@Composable
private fun formatHumanAge(years: Int, months: Int): String {
    return when {
        years == 0 -> {
            val m = pluralStringResource(R.plurals.month, months, months)
            stringResource(R.string.time_month, m)
        }
        months == 0 -> {
            val y = pluralStringResource(R.plurals.year, years, years)
            stringResource(R.string.time_year, y)
        }
        else -> {
            val m = pluralStringResource(R.plurals.month, months, months)
            val y = pluralStringResource(R.plurals.year, years, years)
            stringResource(R.string.time_year_month, y, m)
        }
    }
}
