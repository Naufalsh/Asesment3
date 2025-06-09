package com.naufalmaulanaartocarpussavero607062300078.asesment3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naufalmaulanaartocarpussavero607062300078.asesment3.MainScreen
import com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.screen.HomeScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.myFilm.route) {
            MainScreen(navController)
        }
    }
}