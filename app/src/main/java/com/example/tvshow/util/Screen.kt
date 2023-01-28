package com.example.tvshow.util

sealed class Screen(val route: String) {
    object SearchScreen : Screen("search_screen")
}