package com.alvaroquintana.edadperruna.ui.breedDescription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.common.UiState
import androidx.hilt.navigation.compose.hiltViewModel
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoAsyncImage
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoButton
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoCard
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoCardVariant
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoTopBar
import com.alvaroquintana.edadperruna.core.designsystem.components.ShimmerBox
import com.alvaroquintana.edadperruna.ui.components.AdaptiveContainer
import com.alvaroquintana.edadperruna.ui.components.GradientOverlay
import com.alvaroquintana.edadperruna.ui.components.InfoChip
import com.alvaroquintana.edadperruna.ui.components.NoInternetDialog
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTheme
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens

@Composable
fun BreedDescriptionScreen(
    image: String,
    name: String,
    idBreed: String,
    onBackClick: () -> Unit,
    onSelectBreed: (image: String, name: String, life: String) -> Unit,
    viewModel: BreedDescriptionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showAd by viewModel.showAd.collectAsStateWithLifecycle()
    val showNoInternet by viewModel.showNoInternet.collectAsStateWithLifecycle()
    val favoriteBreed by viewModel.favoriteBreed.collectAsStateWithLifecycle()

    val isLoading = uiState is UiState.Loading
    val breedData = (uiState as? UiState.Success)?.data
    val isFavorite = favoriteBreed?.breedId == idBreed

    LaunchedEffect(idBreed) {
        viewModel.loadBreed(idBreed)
    }

    // No Internet Dialog
    if (showNoInternet) {
        NoInternetDialog(
            onDismiss = { viewModel.dismissNoInternet() },
            onRetry = {
                viewModel.dismissNoInternet()
                viewModel.loadBreed(idBreed)
            }
        )
    }

    val dog = breedData?.let {
        Dog(
            name = name,
            image = image,
            life = it.mainInformation?.lifeExpectancy?.expectancy?.toString() ?: ""
        )
    }

    Scaffold(
        topBar = {
            PerrunoTopBar(
                title = name,
                onBack = onBackClick,
                actions = {
                    IconButton(
                        onClick = {
                            val avg = breedData?.mainInformation?.lifeExpectancy?.expectancy?.toInt() ?: 0
                            viewModel.toggleFavorite(idBreed, name, image, avg)
                        },
                        enabled = breedData != null,
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                            contentDescription = stringResource(
                                if (isFavorite) R.string.cd_unmark_favorite else R.string.cd_mark_favorite,
                            ),
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        AdaptiveContainer(modifier = Modifier.padding(paddingValues)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(PerrunoTokens.Spacing.lg)
                    .padding(bottom = PerrunoTokens.Spacing.huge + PerrunoTokens.Spacing.xl)
            ) {
                // Breed Image Card with themed gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PerrunoTokens.Spacing.huge * 5)
                        .background(Color.White, PerrunoShapes.md)
                ) {
                    PerrunoAsyncImage(
                        imageUrl = image,
                        contentDescription = stringResource(R.string.dog_image),
                        shape = PerrunoShapes.md,
                        modifier = Modifier.fillMaxSize()
                    )

                    GradientOverlay(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(PerrunoTokens.Spacing.huge + PerrunoTokens.Spacing.xl)
                            .align(Alignment.BottomCenter),
                        startColor = PerrunoTheme.colors.gradientOverlayStart,
                        endColor = PerrunoTheme.colors.gradientOverlayEnd
                    )

                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(PerrunoTokens.Spacing.lg)
                    )
                }

                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                if (isLoading) {
                    // Shimmer loading layout
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(PerrunoTokens.Spacing.huge + PerrunoTokens.Spacing.xxxl),
                        shape = PerrunoShapes.md
                    )
                    Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(PerrunoTokens.Spacing.huge * 3),
                        shape = PerrunoShapes.md
                    )
                } else {
                    breedData?.let { breed ->
                        // Info chips — Size and Life Expectancy
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                PerrunoTokens.Spacing.md,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            breed.mainInformation?.sizeBreed?.let { sizeValue ->
                                InfoChip(
                                    icon = Icons.Rounded.Pets,
                                    value = sizeValue,
                                    label = stringResource(R.string.size),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            breed.mainInformation?.lifeExpectancy?.let { life ->
                                InfoChip(
                                    icon = Icons.Rounded.Schedule,
                                    value = stringResource(
                                        R.string.life_expectative_text,
                                        life.expectancy ?: 0
                                    ),
                                    label = stringResource(R.string.life_expectative),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))

                        // Description card
                        if (breed.shortDescription.isNotEmpty()) {
                            PerrunoCard(
                                variant = PerrunoCardVariant.Elevated,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = breed.shortDescription,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))
                        }

                        // FCI Card
                        if (breed.fci?.group != 0L) {
                            PerrunoCard(
                                variant = PerrunoCardVariant.Elevated,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "FCI",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = PerrunoTokens.Spacing.sm)
                                )

                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.sm))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.fci_group, breed.fci?.group?.toInt() ?: 0),
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(PerrunoTokens.Spacing.huge + PerrunoTokens.Spacing.xxxl)
                                    )
                                    Text(
                                        text = breed.fci?.groupType ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = PerrunoTokens.Spacing.sm)
                                    )
                                }

                                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.sm))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.sm))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.fci_section, breed.fci?.section?.toInt() ?: 0),
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(PerrunoTokens.Spacing.huge + PerrunoTokens.Spacing.xxxl)
                                    )
                                    Text(
                                        text = breed.fci?.sectionType ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = PerrunoTokens.Spacing.sm)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))
                        }

                        // Other Names Card
                        if (breed.otherNames.isNotEmpty()) {
                            InfoCard(
                                title = stringResource(R.string.other_names),
                                content = breed.otherNames.joinToString(", ")
                            )
                            Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.lg))
                        }

                        // Common Diseases Card
                        if (breed.commonDiseases.isNotEmpty()) {
                            InfoCard(
                                title = stringResource(R.string.common_diseases),
                                content = breed.commonDiseases.joinToString(", ")
                            )
                        }
                    }
                }
            }

            // Select Breed Button — pinned to bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = PerrunoTokens.Spacing.lg, vertical = PerrunoTokens.Spacing.md)
            ) {
                PerrunoButton(
                    text = stringResource(R.string.select_breed),
                    onClick = {
                        dog?.let {
                            onSelectBreed(it.image, it.name, it.life)
                        }
                    },
                    icon = ImageVector.vectorResource(R.drawable.ic_check),
                    enabled = dog != null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String
) {
    PerrunoCard(
        variant = PerrunoCardVariant.Elevated,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PerrunoTokens.Spacing.sm)
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.height(PerrunoTokens.Spacing.sm))

        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
