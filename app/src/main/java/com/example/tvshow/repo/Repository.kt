package com.example.tvshow.repo

import com.example.tvshow.model.SearchResponse
import com.example.tvshow.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun searchTVShow(searchTerm: String): Flow<Resource<SearchResponse>>
}