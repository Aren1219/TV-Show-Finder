package com.example.tvshow.remote

import com.example.tvshow.model.SearchResponse
import com.example.tvshow.remote.ApiReferences.SEARCH_TV_ENDPOINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET(SEARCH_TV_ENDPOINT)
    suspend fun searchTV(
        @Query("q") searchTerm: String
    ): Response<SearchResponse>
}