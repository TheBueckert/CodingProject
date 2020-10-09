package com.clyde.codingchallenge.API

import com.clyde.codingchallenge.models.MovieItem
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// this interface is implemented when retrofit builders are called executes a call on a coroutine thread
// requires the URL to be complete with search terms before calling
interface ApiService {
    @GET("{BuiltURL}")
    suspend fun getMovies(
        @Path("BuiltURL") BuiltURL:String
    ):MovieItem
}
