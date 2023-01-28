package com.example.tvshow.util

sealed class Screen(val route: String, val path: String = "") {
    object SearchScreen : Screen(route = "search_screen")
    object DetailScreen: Screen(route = "detail/{id}", path = "detail/")
}