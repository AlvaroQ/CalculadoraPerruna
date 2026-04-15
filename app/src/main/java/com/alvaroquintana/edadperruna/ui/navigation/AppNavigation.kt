package com.alvaroquintana.edadperruna.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionScreen
import com.alvaroquintana.edadperruna.ui.breedList.BreedListScreen
import com.alvaroquintana.edadperruna.ui.home.HomeScreen
import com.alvaroquintana.edadperruna.ui.result.ResultScreen
import com.alvaroquintana.edadperruna.ui.settings.SettingsScreen
import com.alvaroquintana.edadperruna.ui.theme.PerrunoTokens
import kotlinx.serialization.Serializable

// Type-safe route definitions — no URL encoding needed
@Serializable data object HomeRoute
@Serializable data object SettingsRoute
@Serializable data object BreedListRoute
@Serializable data class BreedDescriptionRoute(val image: String, val name: String, val idBreed: String)
@Serializable data class ResultRoute(val years: Int, val months: Int, val image: String, val name: String, val life: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // State to hold selected breed data across navigation
    var selectedBreedImage by rememberSaveable { mutableStateOf("") }
    var selectedBreedName by rememberSaveable { mutableStateOf("") }
    var selectedBreedLife by rememberSaveable { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(PerrunoTokens.Motion.MEDIUM_MS)
            ) + fadeIn(animationSpec = tween(PerrunoTokens.Motion.MEDIUM_MS))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth / 3 },
                animationSpec = tween(PerrunoTokens.Motion.MEDIUM_MS)
            ) + fadeOut(animationSpec = tween(PerrunoTokens.Motion.SHORT_MS))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth / 3 },
                animationSpec = tween(PerrunoTokens.Motion.MEDIUM_MS)
            ) + fadeIn(animationSpec = tween(PerrunoTokens.Motion.MEDIUM_MS))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(PerrunoTokens.Motion.MEDIUM_MS)
            ) + fadeOut(animationSpec = tween(PerrunoTokens.Motion.SHORT_MS))
        },
    ) {
        composable<HomeRoute> {
            HomeScreen(
                selectedBreedImage = selectedBreedImage,
                selectedBreedName = selectedBreedName,
                selectedBreedLife = selectedBreedLife,
                onSettingsClick = {
                    navController.navigate(SettingsRoute)
                },
                onSelectBreedClick = {
                    navController.navigate(BreedListRoute)
                },
                onCalculateClick = { years, months, img, nm, lf ->
                    navController.navigate(ResultRoute(years, months, img, nm, lf))
                },
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<BreedListRoute> {
            BreedListScreen(
                onBackClick = { navController.popBackStack() },
                onBreedSelected = { breedId, image, name, life ->
                    selectedBreedImage = image
                    selectedBreedName = name
                    selectedBreedLife = life
                    navController.navigate(BreedDescriptionRoute(image, name, breedId)) {
                        popUpTo<BreedListRoute> { inclusive = true }
                    }
                },
            )
        }

        composable<BreedDescriptionRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<BreedDescriptionRoute>()
            BreedDescriptionScreen(
                image = route.image,
                name = route.name,
                idBreed = route.idBreed,
                onBackClick = { navController.popBackStack() },
                onSelectBreed = { img, nm, life ->
                    selectedBreedImage = img
                    selectedBreedName = nm
                    selectedBreedLife = life
                    navController.navigate(HomeRoute) {
                        popUpTo<HomeRoute> { inclusive = true }
                    }
                },
            )
        }

        composable<ResultRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ResultRoute>()
            ResultScreen(
                years = route.years,
                months = route.months,
                image = route.image,
                name = route.name,
                life = route.life,
                onBackClick = { navController.popBackStack() },
                onTryAgainClick = {
                    selectedBreedImage = ""
                    selectedBreedName = ""
                    selectedBreedLife = ""
                    navController.navigate(HomeRoute) {
                        popUpTo<HomeRoute> { inclusive = true }
                    }
                },
            )
        }
    }
}
