package com.clyde.codingchallenge.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// this singleton class will create a single retrofit object that is used to execute API calls
// it returns objects as a java Object in the form of a data class within models
object MyRetrofitBuilder{

    const val BASE_URL = "https://itunes.apple.com/search/"
    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val apiService: ApiService by lazy {
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}