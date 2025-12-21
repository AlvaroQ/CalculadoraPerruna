package com.alvaroquintana.edadperruna.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alvaroquintana.edadperruna.ui.breedDescription.BreedDescriptionScreen
import com.alvaroquintana.edadperruna.ui.breedList.BreedListScreen
import com.alvaroquintana.edadperruna.ui.home.HomeScreen
import com.alvaroquintana.edadperruna.ui.result.ResultScreen
import com.alvaroquintana.edadperruna.ui.settings.SettingsScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Home : Screen("home?image={image}&name={name}&life={life}") {
        fun createRoute(image: String = "", name: String = "", life: String = ""): String {
            val encodedImage = URLEncoder.encode(image, StandardCharsets.UTF_8.toString())
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
            val encodedLife = URLEncoder.encode(life, StandardCharsets.UTF_8.toString())
            return "home?image=$encodedImage&name=$encodedName&life=$encodedLife"
        }
    }
    object Settings : Screen("settings")
    object BreedList : Screen("breedList")
    object BreedDescription : Screen("breedDescription/{image}/{name}/{idBreed}") {
        fun createRoute(image: String, name: String, idBreed: String): String {
            val encodedImage = URLEncoder.encode(image, StandardCharsets.UTF_8.toString())
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
            return "breedDescription/$encodedImage/$encodedName/$idBreed"
        }
    }
    object Result : Screen("result/{years}/{months}/{image}/{name}/{life}") {
        fun createRoute(years: Int, months: Int, image: String, name: String, life: String): String {
            val encodedImage = URLEncoder.encode(image, StandardCharsets.UTF_8.toString())
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
            val encodedLife = URLEncoder.encode(life, StandardCharsets.UTF_8.toString())
            return "result/$years/$months/$encodedImage/$encodedName/$encodedLife"
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // State to hold selected breed data
    var selectedBreedImage by rememberSaveable { mutableStateOf("") }
    var selectedBreedName by rememberSaveable { mutableStateOf("") }
    var selectedBreedLife by rememberSaveable { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.createRoute()
    ) {
        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("image") { type = NavType.StringType; defaultValue = "" },
                navArgument("name") { type = NavType.StringType; defaultValue = "" },
                navArgument("life") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val image = backStackEntry.arguments?.getString("image")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            } ?: selectedBreedImage
            val name = backStackEntry.arguments?.getString("name")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            } ?: selectedBreedName
            val life = backStackEntry.arguments?.getString("life")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            } ?: selectedBreedLife

            HomeScreen(
                selectedBreedImage = image.ifEmpty { selectedBreedImage },
                selectedBreedName = name.ifEmpty { selectedBreedName },
                selectedBreedLife = life.ifEmpty { selectedBreedLife },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onSelectBreedClick = {
                    navController.navigate(Screen.BreedList.route)
                },
                onCalculateClick = { years, months, img, nm, lf ->
                    navController.navigate(Screen.Result.createRoute(years, months, img, nm, lf))
                }
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.BreedList.route) {
            BreedListScreen(
                onBackClick = { navController.popBackStack() },
                onBreedSelected = { image, name, life ->
                    selectedBreedImage = image
                    selectedBreedName = name
                    selectedBreedLife = life
                    navController.navigate(
                        Screen.BreedDescription.createRoute(image, name, "0")
                    ) {
                        // Remove BreedList from back stack
                        popUpTo(Screen.BreedList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.BreedDescription.route,
            arguments = listOf(
                navArgument("image") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("idBreed") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val image = URLDecoder.decode(
                backStackEntry.arguments?.getString("image") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val name = URLDecoder.decode(
                backStackEntry.arguments?.getString("name") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val idBreed = backStackEntry.arguments?.getString("idBreed") ?: ""

            BreedDescriptionScreen(
                image = image,
                name = name,
                idBreed = idBreed,
                onBackClick = { navController.popBackStack() },
                onSelectBreed = { img, nm, life ->
                    selectedBreedImage = img
                    selectedBreedName = nm
                    selectedBreedLife = life
                    navController.navigate(Screen.Home.createRoute(img, nm, life)) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("years") { type = NavType.IntType },
                navArgument("months") { type = NavType.IntType },
                navArgument("image") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("life") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val years = backStackEntry.arguments?.getInt("years") ?: 0
            val months = backStackEntry.arguments?.getInt("months") ?: 0
            val image = URLDecoder.decode(
                backStackEntry.arguments?.getString("image") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val name = URLDecoder.decode(
                backStackEntry.arguments?.getString("name") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val life = URLDecoder.decode(
                backStackEntry.arguments?.getString("life") ?: "",
                StandardCharsets.UTF_8.toString()
            )

            ResultScreen(
                years = years,
                months = months,
                image = image,
                name = name,
                life = life,
                onBackClick = { navController.popBackStack() },
                onTryAgainClick = {
                    // Clear selected breed and go to home
                    selectedBreedImage = ""
                    selectedBreedName = ""
                    selectedBreedLife = ""
                    navController.navigate(Screen.Home.createRoute()) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
