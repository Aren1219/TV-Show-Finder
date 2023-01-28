package com.example.tvshow.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.tvshow.util.Resource

@Composable
fun SearchScreen(viewModel: MainViewModel) {
    val uiState = viewModel.searchResponse.collectAsState().value
    if (uiState.data?.isEmpty() == true && uiState is Resource.Success)
        Box(modifier = Modifier.fillMaxSize()) {
        }
}