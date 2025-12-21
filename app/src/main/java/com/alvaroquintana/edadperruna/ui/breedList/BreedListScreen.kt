package com.alvaroquintana.edadperruna.ui.breedList

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun BreedListScreen(
    onBackClick: () -> Unit,
    onBreedSelected: (image: String, name: String, life: String) -> Unit,
    onBreedLongClick: (image: String) -> Unit = {},
    viewModel: BreedListViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val progress by viewModel.progress.observeAsState(false)
    val breedList by viewModel.list.observeAsState(mutableListOf())
    val navigation by viewModel.navigation.observeAsState()

    var searchText by remember { mutableStateOf("") }
    var isOrderDescending by remember { mutableStateOf(false) }
    var spanCount by remember { mutableIntStateOf(3) }
    var filterRotation by remember { mutableStateOf(0f) }

    val animatedRotation by animateFloatAsState(
        targetValue = filterRotation,
        label = "filterRotation"
    )

    // Filter and sort breeds
    val displayedBreeds = remember(breedList, searchText, isOrderDescending) {
        var filtered = if (searchText.isEmpty()) {
            breedList.toMutableList()
        } else {
            breedList.filter { dog ->
                dog.name?.uppercase(Locale.ROOT)?.contains(searchText.uppercase(Locale.ROOT)) == true ||
                dog.otherNames?.toString()?.uppercase(Locale.ROOT)?.contains(searchText.uppercase(Locale.ROOT)) == true
            }.toMutableList()
        }

        if (isOrderDescending) {
            filtered.sortByDescending { it.name }
        } else {
            filtered.sortBy { it.name }
        }
        filtered
    }

    // Handle navigation
    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is BreedListViewModel.Navigation.BreedDescription -> {
                    onBreedSelected(
                        it.breed.image ?: "",
                        it.breed.name ?: "",
                        it.breed.life ?: ""
                    )
                }
                is BreedListViewModel.Navigation.Expand -> {
                    onBreedLongClick(it.image)
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.back_btn),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box {
                                if (searchText.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.advance_search),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    IconButton(
                        onClick = {
                            isOrderDescending = !isOrderDescending
                            filterRotation += 180f
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                            contentDescription = stringResource(R.string.advance_search),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.rotate(animatedRotation)
                        )
                    }

                    IconButton(
                        onClick = {
                            spanCount = if (spanCount == 5) 1 else spanCount + 1
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_grid),
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Content
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (progress) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(spanCount),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayedBreeds, key = { it.breedId ?: it.name ?: "" }) { dog ->
                            BreedItem(
                                dog = dog,
                                onClick = { viewModel.onDogClicked(dog) },
                                onLongClick = { dog.image?.let { onBreedLongClick(it) } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BreedItem(
    dog: Dog,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(dog.image)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.dog_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Name
            Text(
                text = dog.name ?: "",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
    }
}
