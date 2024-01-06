package com.dicoding.alficinemacompose.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object DetailCinema : Screen("home/{cinemaId}") {
        fun createRoute(cinemaId: Long) = "home/$cinemaId"
    }
}
