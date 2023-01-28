package com.example.tvshow.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshow.model.SearchResponse
import com.example.tvshow.repo.Repository
import com.example.tvshow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _searchResponse: MutableStateFlow<Resource<SearchResponse>> = MutableStateFlow(
        Resource.Success(SearchResponse())
    )
    val searchResponse: StateFlow<Resource<SearchResponse>> = _searchResponse
    var shouldShowGrid by mutableStateOf(false)
        private set

    var searchTerm by mutableStateOf("")

    fun searchTVShow() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchTVShow(searchTerm).collect {
                _searchResponse.value = it
                shouldShowGrid = !it.data.isNullOrEmpty()
            }
        }
    }
}