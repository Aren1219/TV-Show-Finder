package com.example.tvshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tvshow.ui.DetailScreen
import com.example.tvshow.ui.MainViewModel
import com.example.tvshow.ui.SearchScreen
import com.example.tvshow.ui.theme.TVShowTheme
import com.example.tvshow.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel()
            TVShowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SearchScreen.route
                    ) {
                        composable(Screen.SearchScreen.route) {
                            SearchScreen(viewModel, navController)
                        }
                        composable(
                            Screen.DetailScreen.route,
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) {
                            it.arguments?.getString("id")?.let { id ->
                                DetailScreen(
                                    viewModel = viewModel,
                                    navController = navController,
                                    id = id
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
