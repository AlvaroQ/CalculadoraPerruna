package com.alvaroquintana.edadperruna.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.alvaroquintana.edadperruna.ui.components.AdaptiveContainer
import com.alvaroquintana.edadperruna.ui.components.PerrunoAsyncImage
import com.alvaroquintana.edadperruna.ui.components.PerrunoButton
import com.alvaroquintana.edadperruna.ui.components.PerrunoButtonSize
import com.alvaroquintana.edadperruna.ui.components.PerrunoTopBar
import com.alvaroquintana.edadperruna.ui.theme.DynaPuffFontFamily
import com.alvaroquintana.edadperruna.ui.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.ui.theme.PerrunoTheme
import com.alvaroquintana.edadperruna.ui.theme.PerrunoTokens

@Composable
fun HomeScreen(
    selectedBreedImage: String = "",
    selectedBreedName: String = "",
    selectedBreedLife: String = "",
    onSettingsClick: () -> Unit,
    onSelectBreedClick: () -> Unit,
    onCalculateClick: (years: Int, months: Int, image: String, name: String, life: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var yearText by remember { mutableStateOf("") }
    var monthText by remember { mutableStateOf("") }
    var yearError by remember { mutableStateOf<String?>(null) }
    var monthError by remember { mutableStateOf<String?>(null) }
    var breedError by remember { mutableStateOf(false) }

    // Animation states
    var animationStarted by remember { mutableStateOf(false) }
    val headerAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(PerrunoTokens.Motion.LONG_MS),
        label = "headerAlpha"
    )
    val breedCardAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(PerrunoTokens.Motion.LONG_MS, delayMillis = PerrunoTokens.Motion.STAGGER_MS * 2),
        label = "breedCardAlpha"
    )
    val inputAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(PerrunoTokens.Motion.LONG_MS, delayMillis = PerrunoTokens.Motion.STAGGER_MS * 3),
        label = "inputAlpha"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(PerrunoTokens.Motion.LONG_MS, delayMillis = PerrunoTokens.Motion.STAGGER_MS * 5),
        label = "buttonAlpha"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    // Handle events (navigation + errors) via Channel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeViewModel.HomeEvent.ShowError -> {
                    when (event.error) {
                        HomeViewModel.HomeError.BreedEmpty -> breedError = true
                        HomeViewModel.HomeError.YearEmpty -> yearError = context.getString(R.string.fill_year)
                        HomeViewModel.HomeError.MonthEmpty -> monthError = context.getString(R.string.fill_month)
                        HomeViewModel.HomeError.MonthIllegal -> monthError = context.getString(R.string.ilegal_month)
                    }
                }
                HomeViewModel.HomeEvent.NavigateToBreedList -> onSelectBreedClick()
                is HomeViewModel.HomeEvent.NavigateToResult -> {
                    onCalculateClick(
                        yearText.toIntOrNull() ?: 0,
                        monthText.toIntOrNull() ?: 0,
                        event.breed.image,
                        event.breed.name,
                        event.breed.life
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            PerrunoTopBar(
                title = stringResource(R.string.app_name),
                actions = {
                    androidx.compose.material3.IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AdaptiveContainer(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = PerrunoTokens.Spacing.lg, end = PerrunoTokens.Spacing.lg, top = PerrunoTokens.Spacing.sm, bottom = PerrunoTokens.Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.xl)
        ) {
            // Hero Card
            HeroCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = headerAlpha }
            )

            // Breed Selector
            BreedSelectorCard(
                selectedBreedImage = selectedBreedImage,
                selectedBreedName = selectedBreedName,
                hasError = breedError,
                onClick = {
                    breedError = false
                    viewModel.navigateToBreedList()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = breedCardAlpha }
            )

            // Age Input Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = inputAlpha },
                verticalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.md)
            ) {
                Text(
                    text = stringResource(R.string.age_section_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = DynaPuffFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.md)
                ) {
                    AgeInputCard(
                        label = stringResource(R.string.year_title),
                        value = yearText,
                        onValueChange = {
                            yearText = it
                            yearError = null
                        },
                        hasError = yearError != null,
                        errorText = yearError,
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        modifier = Modifier.weight(1f)
                    )

                    AgeInputCard(
                        label = stringResource(R.string.month_title),
                        value = monthText,
                        onValueChange = {
                            monthText = it
                            monthError = null
                        },
                        hasError = monthError != null,
                        errorText = monthError,
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Calculate Button
            PerrunoButton(
                text = stringResource(R.string.calculate_human_age),
                onClick = {
                    breedError = false
                    yearError = null
                    monthError = null
                    val dog = Dog(
                        image = selectedBreedImage,
                        name = selectedBreedName,
                        life = selectedBreedLife
                    )
                    if (viewModel.checkErrors(dog, yearText, monthText)) {
                        viewModel.navigateToResult(dog)
                    }
                },
                icon = ImageVector.vectorResource(R.drawable.ic_calculate),
                size = PerrunoButtonSize.Large,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = buttonAlpha }
            )

            // Bottom Hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.formula_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        }
    }
}

@Composable
private fun HeroCard(modifier: Modifier = Modifier) {
    val perrunoColors = PerrunoTheme.colors
    val gradientStart = perrunoColors.buttonGradientStart
    val gradientEnd = perrunoColors.buttonGradientEnd
    val contentColor = perrunoColors.buttonContent

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = PerrunoShapes.lg
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(gradientStart, gradientEnd)
                ),
                shape = PerrunoShapes.lg
            )
            .padding(PerrunoTokens.Spacing.xl)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.sm)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_paw),
                contentDescription = null,
                tint = contentColor.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = stringResource(R.string.hero_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = DynaPuffFontFamily,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                ),
                color = contentColor
            )

            Text(
                text = stringResource(R.string.hero_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun BreedSelectorCard(
    selectedBreedImage: String,
    selectedBreedName: String,
    hasError: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = PerrunoShapes.md
            )
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        shape = PerrunoShapes.md,
        border = if (hasError) {
            BorderStroke(PerrunoTokens.Elevation.sm, MaterialTheme.colorScheme.error)
        } else {
            BorderStroke(PerrunoTokens.Elevation.xs, MaterialTheme.colorScheme.outlineVariant)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = PerrunoTokens.Spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Breed image or paw icon placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = PerrunoShapes.sm
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedBreedImage.isNotEmpty()) {
                    PerrunoAsyncImage(
                        imageUrl = selectedBreedImage,
                        contentDescription = stringResource(R.string.dog_image),
                        shape = PerrunoShapes.sm,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_paw),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(PerrunoTokens.Spacing.md))

            // Breed text column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.breed),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (selectedBreedName.isNotEmpty()) selectedBreedName else stringResource(R.string.select_breed),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Chevron
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_expand_more),
                contentDescription = stringResource(R.string.expand),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AgeInputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hasError: Boolean,
    errorText: String?,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            shape = PerrunoShapes.md,
            border = if (hasError) {
                BorderStroke(PerrunoTokens.Elevation.sm, MaterialTheme.colorScheme.error)
            } else {
                BorderStroke(PerrunoTokens.Elevation.xs, MaterialTheme.colorScheme.outlineVariant)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = PerrunoTokens.Spacing.lg, vertical = PerrunoTokens.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(PerrunoTokens.Spacing.sm))
                    BasicTextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                onValueChange(newValue)
                            }
                        },
                        textStyle = TextStyle(
                            fontFamily = DynaPuffFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (value.isEmpty()) {
                                Text(
                                    text = "0",
                                    style = TextStyle(
                                        fontFamily = DynaPuffFontFamily,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }

        if (hasError && errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = PerrunoTokens.Spacing.xs, top = PerrunoTokens.Spacing.xs)
            )
        }
    }
}
