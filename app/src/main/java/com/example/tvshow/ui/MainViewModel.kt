package com.example.tvshow.ui

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

    fun searchTVShow(searchTerm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchTVShow(searchTerm).collect {
                _searchResponse.value = it
            }
        }
    }
}