package com.alvaroquintana.edadperruna.ui.breedList

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.core.designsystem.theme.PerrunoTokens
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionScreen
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * Adaptive list-detail scaffold for breed browsing.
 *
 * - Compact (phone portrait): scaffold shows one pane at a time and slides
 *   between list and detail — visually equivalent to the previous NavHost flow.
 * - Medium / Expanded (tablet, foldable open, landscape): list and detail are
 *   visible simultaneously. Picking a breed updates the detail pane in place
 *   instead of pushing a new screen.
 *
 * The selection is held by [ThreePaneScaffoldNavigator] so configuration
 * changes (rotation, fold/unfold) preserve which breed is being inspected.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreedListAdaptiveScreen(
    onBackClick: () -> Unit,
    onSelectBreed: (image: String, name: String, life: String) -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<BreedSelection>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch { navigator.navigateBack() }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                BreedListScreen(
                    onBackClick = onBackClick,
                    onBreedSelected = { breedId, image, name, life ->
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = BreedSelection(breedId, image, name, life),
                            )
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selection = navigator.currentDestination?.contentKey
                if (selection != null) {
                    BreedDescriptionScreen(
                        image = selection.image,
                        name = selection.name,
                        idBreed = selection.breedId,
                        onBackClick = {
                            scope.launch { navigator.navigateBack() }
                        },
                        onSelectBreed = onSelectBreed,
                    )
                } else {
                    EmptyDetailPlaceholder()
                }
            }
        },
    )
}

@Composable
private fun EmptyDetailPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PerrunoTokens.Spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Rounded.Pets,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.adaptive_select_breed_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = PerrunoTokens.Spacing.md),
            )
        }
    }
}

// @Parcelize (not @Serializable) because ThreePaneScaffoldNavigator's default
// saver stores the contentKey via rememberSaveable, which accepts Parcelable
// or java.io.Serializable — not kotlinx.serialization's @Serializable. A crash
// was observed on configuration change after picking a breed (#30 aftermath).
@Parcelize
data class BreedSelection(
    val breedId: String,
    val image: String,
    val name: String,
    val life: String,
) : Parcelable
