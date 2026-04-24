package com.alvaroquintana.edadperruna.ui.breedList

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.ui.common.UiState
import com.alvaroquintana.edadperruna.core.designsystem.components.PerrunoSearchBar
import com.alvaroquintana.edadperruna.core.designsystem.components.ShimmerBox
import com.alvaroquintana.edadperruna.ui.components.AdaptiveContainer
import com.alvaroquintana.edadperruna.ui.components.BreedGridItem
import com.alvaroquintana.edadperruna.ui.components.NoInternetDialog
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoShapes
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens
import java.util.Locale

private const val STAGGER_CAP = 20

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BreedListScreen(
    onBackClick: () -> Unit,
    onBreedSelected: (breedId: String, image: String, name: String, life: String) -> Unit,
    onBreedLongClick: (image: String) -> Unit = {},
    viewModel: BreedListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showAd by viewModel.showAd.collectAsStateWithLifecycle()
    val showNoInternet by viewModel.showNoInternet.collectAsStateWithLifecycle()

    val isLoading = uiState is UiState.Loading
    val isError = uiState is UiState.Error
    val breedList = (uiState as? UiState.Success)?.data ?: emptyList()

    // Default span count adapts to window width — 3 cols on phones, 5 on tablets/foldables.
    // The grid toggle in PerrunoSearchBar still lets the user override per session.
    val widthSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val initialSpanCount = remember(widthSizeClass) {
        when (widthSizeClass) {
            WindowWidthSizeClass.EXPANDED -> 5
            WindowWidthSizeClass.MEDIUM -> 4
            else -> 3
        }
    }

    var searchText by remember { mutableStateOf("") }
    var isOrderDescending by remember { mutableStateOf(false) }
    var spanCount by remember(initialSpanCount) { mutableIntStateOf(initialSpanCount) }

    // Filter and sort breeds
    val displayedBreeds = remember(breedList, searchText, isOrderDescending) {
        var filtered = if (searchText.isEmpty()) {
            breedList.toMutableList()
        } else {
            breedList.filter { dog ->
                dog.name.uppercase(Locale.ROOT).contains(searchText.uppercase(Locale.ROOT)) ||
                dog.otherNames.toString().uppercase(Locale.ROOT).contains(searchText.uppercase(Locale.ROOT))
            }.toMutableList()
        }

        if (isOrderDescending) {
            filtered.sortByDescending { it.name }
        } else {
            filtered.sortBy { it.name }
        }
        filtered
    }

    // Handle events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BreedListViewModel.BreedListEvent.NavigateToDescription -> {
                    onBreedSelected(
                        event.dog.breedId,
                        event.dog.image,
                        event.dog.name,
                        event.dog.life
                    )
                }
                is BreedListViewModel.BreedListEvent.ExpandImage -> {
                    onBreedLongClick(event.imageUrl)
                }
            }
        }
    }

    // No Internet Dialog
    if (showNoInternet) {
        NoInternetDialog(
            onDismiss = { viewModel.dismissNoInternet() },
            onRetry = {
                viewModel.dismissNoInternet()
                viewModel.loadBreeds()
            }
        )
    }

    Scaffold { paddingValues ->
        AdaptiveContainer(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PerrunoSearchBar(
                query = searchText,
                onQueryChange = { searchText = it },
                onBack = onBackClick,
                onSortToggle = { isOrderDescending = !isOrderDescending },
                isSortDescending = isOrderDescending,
                onGridToggle = { spanCount = if (spanCount == 5) 1 else spanCount + 1 },
                spanCount = spanCount
            )

            when {
                isLoading -> {
                    // Shimmer placeholder grid matching the real grid layout
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(spanCount),
                        contentPadding = PaddingValues(
                            horizontal = PerrunoTokens.Spacing.sm,
                            vertical = PerrunoTokens.Spacing.sm
                        ),
                        horizontalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.xs),
                        verticalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.xs),
                        modifier = Modifier.fillMaxSize(),
                        userScrollEnabled = false
                    ) {
                        items(spanCount * 6) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.75f),
                                shape = PerrunoShapes.md
                            )
                        }
                    }
                }
                isError -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.error_loading_data),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(PerrunoTokens.Spacing.xl)
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(spanCount),
                        contentPadding = PaddingValues(
                            horizontal = PerrunoTokens.Spacing.sm,
                            vertical = PerrunoTokens.Spacing.sm
                        ),
                        horizontalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.xs),
                        verticalArrangement = Arrangement.spacedBy(PerrunoTokens.Spacing.xs),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            displayedBreeds,
                            key = { _, dog -> dog.breedId.ifEmpty { dog.name } }
                        ) { index, dog ->
                            StaggeredBreedItem(
                                index = index,
                                dog = dog,
                                onClick = { viewModel.onDogClicked(dog) },
                                onLongClick = {
                                    if (dog.image.isNotEmpty()) viewModel.onDogLongClicked(dog.image)
                                }
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StaggeredBreedItem(
    index: Int,
    dog: com.alvaroquintana.edadperruna.core.domain.model.Dog,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val cappedIndex = index.coerceAtMost(STAGGER_CAP - 1)
    val delayMs = cappedIndex * PerrunoTokens.Motion.STAGGER_MS

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(dog.breedId) {
        kotlinx.coroutines.delay(delayMs.toLong())
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = PerrunoTokens.Motion.MEDIUM_MS),
        label = "breedItemAlpha_$index"
    )

    // Wrap in Box so combinedClickable handles both tap and long-press,
    // while BreedGridItem receives a no-op to avoid double-firing.
    Box(
        modifier = Modifier
            .alpha(alpha)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        BreedGridItem(
            name = dog.name,
            imageUrl = dog.image,
        )
    }
}
