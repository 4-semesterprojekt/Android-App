package com.example.denfriebible

import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun DenFrieBibleNavHost(navController: NavHostController,
            modifier: Modifier = Modifier){
    NavHost(
        navController = navController,
        startDestination = DefaultView.route,
        modifier = modifier
    ) {
        composable(DefaultView.route){
            DefaultView(navController)
        }

        composable(
            route = GetBook.route,
            arguments = listOf(
                navArgument("number") {
                    type = NavType.StringType
                },
                navArgument("abbreviation") {
                    type = NavType.StringType
                }
            )
        ) {
            val number = it.arguments?.getString("number")!!
            val abbreviation = it.arguments?.getString("abbreviation")!!

            GetBook(
                navController = navController,
                number = number,
                abbreviation = abbreviation,
            )
        }
        composable(
            route = GetChapter.route,
            arguments = listOf(
                navArgument("abbreviation") {
                    type = NavType.StringType
                }
            )
        ) {
            val abbreviation = it.arguments?.getString("abbreviation")!!

            GetChapter(
                navController = navController,
                abbreviation = abbreviation,
            )
        }
    }
}
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }