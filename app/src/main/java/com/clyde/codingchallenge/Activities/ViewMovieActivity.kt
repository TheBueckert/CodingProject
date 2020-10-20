package com.clyde.codingchallenge.Activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.clyde.codingchallenge.R
import com.clyde.codingchallenge.ViewModels.ViewMovieViewModel
import com.clyde.codingchallenge.models.Result
import kotlinx.android.synthetic.main.activity_view_movie.*

class ViewMovieActivity : AppCompatActivity() {

    private lateinit var viewMovieViewModel: ViewMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movie)
        // instantiate ViewModel ASAP
        viewMovieViewModel = ViewModelProvider(this).get(ViewMovieViewModel::class.java)
        setUpViewModel()
        setToolbar()
        setUpView()
    }

    // The object for the ViewModel is passed as a parcellable extra. If it is null, close the activity
    private fun setUpViewModel() {
        val b = intent.extras
        if (b != null) {

            if (b.get("result") != null) {

                viewMovieViewModel.result = b.get("result") as Result?

            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    // add a back button and remove the title from the toolbar
    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    // if the button clicked is the back button, finish activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    // get all relevant information from viewmodel. Will persist between screen changes
    private fun setUpView() {
        // load default img to display while loading
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.loading_preview_image)
            .error(R.drawable.loading_preview_image)
        Glide.with(this)
            .applyDefaultRequestOptions(requestOptions)
            .load(viewMovieViewModel.getImage())
            .into(movie_imageview)

        title_textview.text = viewMovieViewModel.getTitle()
        genre_textview.text = viewMovieViewModel.getGenre()
        actors_names_textview.text = viewMovieViewModel.getCast()
        description_textview.text = viewMovieViewModel.getDescription()

    }
}