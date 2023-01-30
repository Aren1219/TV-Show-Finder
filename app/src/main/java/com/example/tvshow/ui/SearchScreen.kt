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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tvshow.R
import com.example.tvshow.datastore.StoreSearchTerm
import com.example.tvshow.model.SearchResponse
import com.example.tvshow.model.SearchResponseItem
import com.example.tvshow.util.Resource
import com.example.tvshow.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: MainViewModel, navController: NavHostController) {
    val uiState = viewModel.searchResponse.collectAsState().value
    val shouldShowGrid = viewModel.shouldShowGrid
    val focusManager = LocalFocusManager.current

//    var shouldShowHistory by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreSearchTerm(context)
    val history = dataStore.getSearchTerm.collectAsState(initial = "").value

    fun searchTVShow() {
        focusManager.clearFocus()
        viewModel.searchTVShow()
        scope.launch { dataStore.saveSearchTerm(viewModel.searchTerm) }
    }

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
                    value = viewModel.searchTerm,
                    onValueChange = { viewModel.searchTerm = it },
                    placeholder = { Text(text = "Search", color = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = {
                            searchTVShow()
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        searchTVShow()
                    })
                )
                if (!history.isNullOrEmpty())
                    Text(
                        text = "Last search query: $history",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .clickable { viewModel.searchTerm = history }
                    )
                if (shouldShowGrid)
                    uiState.data?.let {
                        TVShowGrid(tvShows = it) { item ->
                            navController.navigate(Screen.DetailScreen.base + item.show.id)
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