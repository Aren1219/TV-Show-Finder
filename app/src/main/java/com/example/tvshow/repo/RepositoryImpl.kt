package com.example.tvshow.repo

import com.example.tvshow.model.SearchResponse
import com.example.tvshow.remote.Api
import com.example.tvshow.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RepositoryImpl(
    private val api: Api
) : Repository {
    override suspend fun searchTVShow(searchTerm: String): Flow<Resource<SearchResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.searchTV(searchTerm)
            if (response.isSuccessful)
                emit(Resource.Success(response.body()!!))
            else
                emit(Resource.Error(response.code().toString()))
        } catch (e: HttpException) {
            emit(Resource.Error("Could not load daily fact"))
        } catch (e: IOException) {
            emit(Resource.Error("Check internet"))
        }
    }
}