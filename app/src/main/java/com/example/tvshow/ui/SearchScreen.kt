package com.example.tvshow.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tvshow.R
import com.example.tvshow.model.SearchResponse
import com.example.tvshow.model.SearchResponseItem
import com.example.tvshow.util.Resource
import com.example.tvshow.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: MainViewModel, navController: NavHostController) {
    val uiState = viewModel.searchResponse.collectAsState().value
    var searchTerm by remember { mutableStateOf("") }
    val shouldShowGrid = viewModel.shouldShowGrid
    val focusManager = LocalFocusManager.current
    when (uiState) {
        is Resource.Success -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement =
                if (!shouldShowGrid) Arrangement.Center
                else Arrangement.Top
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = if (shouldShowGrid) 12.dp else 0.dp,
                            end = 12.dp
                        ),
                    value = searchTerm,
                    onValueChange = { searchTerm = it },
                    placeholder = { Text(text = "Search", color = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = {
                            focusManager.clearFocus()
                            viewModel.searchTVShow(searchTerm)
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        viewModel.searchTVShow(searchTerm)
                    })
                )
                if (shouldShowGrid)
                    uiState.data?.let {
                        TVShowGrid(tvShows = it) { item ->
                            navController.navigate(Screen.DetailScreen.path + item.show.id)
                        }
                    }
            }

        }
        is Resource.Error -> {}
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun TVShowGrid(
    tvShows: SearchResponse,
    modifier: Modifier = Modifier,
    onItemClick: (SearchResponseItem) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(items = tvShows) { item ->
            TVShowGridItem(item) { onItemClick(item) }
        }
    }
}

@Composable
private fun TVShowGridItem(
    item: SearchResponseItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model =
            if (item.show.image != null) item.show.image.original
            else R.drawable.placeholder_image,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = item.show.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(6.dp)
        )
    }
}