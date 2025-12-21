package com.alvaroquintana.edadperruna.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    selectedBreedImage: String = "",
    selectedBreedName: String = "",
    selectedBreedLife: String = "",
    onSettingsClick: () -> Unit,
    onSelectBreedClick: () -> Unit,
    onCalculateClick: (years: Int, months: Int, image: String, name: String, life: String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current
    var yearText by remember { mutableStateOf("") }
    var monthText by remember { mutableStateOf("") }
    var yearError by remember { mutableStateOf<String?>(null) }
    var monthError by remember { mutableStateOf<String?>(null) }
    var breedError by remember { mutableStateOf(false) }

    val error by viewModel.error.observeAsState()
    val navigation by viewModel.navigation.observeAsState()

    // Animation states
    var animationStarted by remember { mutableStateOf(false) }
    val headerAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(500),
        label = "headerAlpha"
    )
    val breedCardAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(500, delayMillis = 200),
        label = "breedCardAlpha"
    )
    val yearFieldAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(500, delayMillis = 300),
        label = "yearFieldAlpha"
    )
    val monthFieldAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(500, delayMillis = 400),
        label = "monthFieldAlpha"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(500, delayMillis = 500),
        label = "buttonAlpha"
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    // Handle errors
    LaunchedEffect(error) {
        error?.let {
            when (it) {
                HomeViewModel.Error.ErrorBreedEmpty -> breedError = true
                HomeViewModel.Error.ErrorYearEmpty -> yearError = context.getString(R.string.fill_year)
                HomeViewModel.Error.ErrorMonthEmpty -> monthError = context.getString(R.string.fill_month)
                HomeViewModel.Error.ErrorMonthIlegal -> monthError = context.getString(R.string.ilegal_month)
            }
        }
    }

    // Handle navigation
    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                HomeViewModel.Navigation.BreedList -> onSelectBreedClick()
                is HomeViewModel.Navigation.Result -> {
                    onCalculateClick(
                        yearText.toIntOrNull() ?: 0,
                        monthText.toIntOrNull() ?: 0,
                        it.breed.image ?: "",
                        it.breed.name ?: "",
                        it.breed.life ?: ""
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .graphicsLayer { alpha = headerAlpha },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.section_dog_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
                )
            }

            // Breed Selection Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .graphicsLayer { alpha = breedCardAlpha }
                    .clickable {
                        breedError = false
                        viewModel.navigateToBreedList()
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                shape = RoundedCornerShape(16.dp),
                border = if (breedError) {
                    androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.error)
                } else {
                    androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedBreedImage.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(selectedBreedImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(R.string.dog_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Text(
                        text = if (selectedBreedName.isNotEmpty()) selectedBreedName else stringResource(R.string.select_breed),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_expand_more),
                        contentDescription = stringResource(R.string.expand),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Year Input
            OutlinedTextField(
                value = yearText,
                onValueChange = {
                    yearText = it
                    yearError = null
                },
                label = { Text(stringResource(R.string.year_title)) },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = yearError != null,
                supportingText = yearError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .graphicsLayer { alpha = yearFieldAlpha }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Month Input
            OutlinedTextField(
                value = monthText,
                onValueChange = {
                    monthText = it
                    monthError = null
                },
                label = { Text(stringResource(R.string.month_title)) },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = monthError != null,
                supportingText = monthError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .graphicsLayer { alpha = monthFieldAlpha }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Calculate Button
            Button(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .graphicsLayer { alpha = buttonAlpha },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_calculate),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.calculate),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
