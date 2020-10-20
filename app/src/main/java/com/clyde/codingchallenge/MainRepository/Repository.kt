package com.clyde.codingchallenge.MainRepository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.clyde.codingchallenge.API.MyRetrofitBuilder
import com.clyde.codingchallenge.models.MovieItem
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception
import javax.security.auth.login.LoginException

// This is initialized as a singleton class so only one instance is using resources
// a job is initialized for the coroutine that will create a retrofit builder and api request on a separate thread
object Repository {

    var job: CompletableJob? = null

    // URLs for the query are completed here. Requires string as a parameter
    fun getMovies(movieName: String): LiveData<MovieItem>{
        job = Job()
        return object: LiveData<MovieItem>(){
            override fun onActive() {
                super.onActive()
                job?.let{current_job->
                    CoroutineScope(IO + current_job).launch {
                        try {
                            val movies =
                                MyRetrofitBuilder.apiService.getMovies("?country=CA&media=movie&entity=movie&limit=20&term=$movieName") // the search term is entered here because retrofit is finicky
                            withContext(Main) {      // the live data returned can't be set on a background thread so a new value is added and returned on the main thread
                                value = movies
                                current_job.complete()
                            }
                            // on issues returned from api call, catch and log error,
                            // return null value for main activity to handle
                        }catch (e:Exception){
                            Log.e("API error", "onActive: ", e  )
                            withContext(Main){
                                value = null
                            }
                            cancelJobs()
                        }
                    }
                }
            }
        }
    }

    // a function to cancel jobs that encountered an error
    fun cancelJobs(){
        job?.cancel()
    }

}