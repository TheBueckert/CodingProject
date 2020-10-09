package com.clyde.codingchallenge.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// This is object is the format the Itunes search api returns queries in
data class MovieItem(
    @Expose
    @SerializedName("resultCount")
    val resultCount: Int,

    @Expose
    @SerializedName("results")
    val results: List<Result>
)