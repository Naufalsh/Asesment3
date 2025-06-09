package com.naufalmaulanaartocarpussavero607062300078.asesment3.navigation

sealed class Screen (val route: String) {
    data object Home: Screen("homeScreen")
    data object myFilm: Screen("mainScreen")

}