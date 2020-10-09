package com.clyde.codingchallenge.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.clyde.codingchallenge.MainRepository.Repository
import com.clyde.codingchallenge.models.MovieItem

// This class is the view model that passes queries from the view to the api handling classes
// It also returns the data from the Api to the view after formatting it properly
class MainActivityViewModel : ViewModel() {
    
    // The search term is modified by the view and starts an api query
    private var viewSearchTerm: MutableLiveData<String> = MutableLiveData()

    // The string required here will become a livedata object, changing it starts the query
    fun setSearchTerm(newSearchTerm: String){
        val update = newSearchTerm
        if(viewSearchTerm.value == update){
            return
        }
        viewSearchTerm.value = update
    }
    
    // The movie data is transformed based on the returns from the api query
    // This livedata changed here is being listened to by the mainActivity
    val moviesQueryResultObject: LiveData<MovieItem> = Transformations
        .switchMap(viewSearchTerm){viewSearchTermString ->
            Repository.getMovies(viewSearchTermString)
        }
    
    

    fun cancelJobs(){
        Repository.cancelJobs()
    }


}
