package com.clyde.codingchallenge.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clyde.codingchallenge.models.Result

// this viewmodel currently takes in a Result passed in from intent, holds it and shares its info with the view
class ViewMovieViewModel() :ViewModel(){
    var result: Result? = null

    fun getTitle() : String? = result?.trackName

    fun getGenre() : String? = result?.primaryGenreName

    fun getCast() : String? = result?.artistName

    fun getDescription() : String? = result?.longDescription

    fun getImage() : String? = result?.artworkUrl100?.let {url-> formatUrl(url) }


    // formats urls for higher res images
    fun formatUrl(imageUrl: String): String{
        return imageUrl.replace("100x100bb", "500x500bb")
    }


}