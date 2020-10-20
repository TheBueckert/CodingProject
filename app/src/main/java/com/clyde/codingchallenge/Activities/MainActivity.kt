package com.clyde.codingchallenge.Activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.clyde.codingchallenge.MainRepository.Repository
import com.clyde.codingchallenge.R
import com.clyde.codingchallenge.RecyclerRelatedClasses.ItemSpacingDecoration
import com.clyde.codingchallenge.RecyclerRelatedClasses.MovieRecyclerAdapter
import com.clyde.codingchallenge.ViewModels.MainActivityViewModel
import com.clyde.codingchallenge.models.Result
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


/*This project has more comments than I would normally leave, but I learned a lot
for this project, so the comments are mainly for explaining to myself how it all works.

This project followed these tutorials:
https://www.youtube.com/watch?v=ijXjCtCXcN4  MVVM tutorial
https://www.youtube.com/watch?v=PQvtdjhQEvw  Kotlin Singletons + Coroutines
https://www.youtube.com/watch?v=rAk1j2CmPJs&feature=emb_title Retrofit REST Client*/



// This class controls the Main_activity Layout as well as the RecyclerView, api calls are offloaded
// The Listener for click activities must implemented from the viewHolder class for clicks on items
class MainActivity : AppCompatActivity(), MovieRecyclerAdapter.MovieViewHolder.OnMovieListener {

    // Initial Variables
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var movieAdapter: MovieRecyclerAdapter
    private var defaultInit: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // realm must be initialized with context for viewholder
        Realm.init(this)
        setListeners()
        setUpViewModel()



    }

    // Included as a way to cancel coroutines of Failed Api calls
    fun cancelJobs() {
        Repository.cancelJobs()
    }

    // This function will initialize a single view model provider and begin observing livedata
    private fun setUpViewModel() {

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        mainActivityViewModel.moviesQueryResultObject.observe(this, Observer {
            // if this is the firstTime initializing the ViewModel it will Initialize the RecyclerView too
            checkRecyclerInitialization()
            // Observing the LiveData, When it changes it will update the recyclerview,  but only using the Results Array for the Recyclerview
            mainActivityViewModel.moviesQueryResultObject.value?.results?.let { resultsArray ->
                if (resultsArray.isNotEmpty()) {
                    movie_recyclerview.visibility = View.VISIBLE
                    no_results_text.visibility = View.GONE
                    movieAdapter.submitList(
                        resultsArray, this
                    )
                    // all search results will be copied to realm
                    mainActivityViewModel.copyToRealm(resultsArray)
                    // if the results array was empty, Display no results Text
                } else {
                    no_results_text.visibility = View.VISIBLE
                    movie_recyclerview.visibility = View.GONE
                }
            }
            // This could be changed to NotifyItemRangeChanged for smoother UX
            movie_recyclerview.adapter?.notifyDataSetChanged()
        })


    }
    // this function will intialize the recyclerview if it isn't created otherwise
    private fun checkRecyclerInitialization(){
        if (defaultInit) {
            showing_results_text.visibility = View.VISIBLE
            initRecyclerView()
            defaultInit = false
            lets_search_text.visibility = View.GONE
        }
    }

    // This recyclerview uses a 2 row grid Layout, and a separate class for applying padding ar runtime
    private fun initRecyclerView() {
        movie_recyclerview.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            val itemSpacingDecoration = ItemSpacingDecoration(30, 30, 30, 30)
            addItemDecoration(itemSpacingDecoration)
            movieAdapter =
                MovieRecyclerAdapter()
            adapter = movieAdapter
        }

    }

    // The listeners are set on the SearchView
    private fun setListeners() {
        // The first Listener allows the whole SearchView to Be clickable
        main_searchview.setOnClickListener {
            main_searchview.isIconified = false
            no_results_text.visibility = View.GONE
        }
        // This Listener will set the live data String that will kick off a query in the ViewModel
        main_searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                main_searchview.clearFocus()

                if (isInternetAvailable()) {
                    if (p0 != null) {
                        showing_results_text.setText("Showing results for: $p0")
                        mainActivityViewModel.setSearchTerm(p0)
                    }
                    // the handling of this function will be modified. The flow of the app is different on different branches
                } else {
                    if (p0 != null) {
                        Toast.makeText(applicationContext, "You are not connected to the internet :(", Toast.LENGTH_LONG).show()
                        val localResults = mainActivityViewModel.searchLocallyInRealm(p0)

                        if(localResults!=null){
                            checkRecyclerInitialization()
                            movie_recyclerview.visibility = View.VISIBLE
                            no_results_text.visibility = View.GONE
                            movieAdapter.submitList(localResults.toList(), this@MainActivity)
                        }

                        else{
                            no_results_text.visibility = View.VISIBLE
                            movie_recyclerview.visibility = View.GONE
                        }

                    }
                    movie_recyclerview.adapter?.notifyDataSetChanged()

                }

                return true
            }


            override fun onQueryTextChange(p0: String?): Boolean = false
        })
    }

    // This is the required implementation of the abstract function for clicks, from the ViewHolder Class

    override fun onMovieClick(result: Result) {

        val bottomSheet = MovieBottomSheetDialog()
        val bundle =
            Bundle()                                                                       // passing data to the BottomSheetDialog
        if (result != null) {
            bundle.putString("title", result.trackName)
            bundle.putString("genre", result.primaryGenreName)
            bundle.putString("cast", result.artistName)
            bundle.putString("description", result.longDescription)
            bundle.putString("img_url", result.artworkUrl100)
            bottomSheet.arguments = bundle
            bottomSheet.show(supportFragmentManager, "Movie Display")
        } else {
            Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show()
        }
    }


    // deprecated libraries from the google tutorial https://developer.android.com/training/monitoring-device-state/connectivity-status-type
    fun isInternetAvailable(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }


}
