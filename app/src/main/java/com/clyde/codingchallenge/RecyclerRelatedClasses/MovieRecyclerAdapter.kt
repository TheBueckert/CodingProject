package com.clyde.codingchallenge.RecyclerRelatedClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.clyde.codingchallenge.R
import com.clyde.codingchallenge.models.Result
import kotlinx.android.synthetic.main.movie_item_cardview.view.*

// The recycler view and the viewholder class are both in this file
//
// This recycler view  requires an external Listener to be implemented to be passed to the viewholder, for on click
// Setting a data set AND the listener should be done with the submitList function
class MovieRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var items: List<Result>
    private lateinit var onMovieListener : MovieViewHolder.OnMovieListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item_cardview,
                parent,
                false
            ), onMovieListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MovieViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    // This Method Requires a dataset, AND an external on item listener for the items action when clicked
    fun submitList(movieList: List<Result>, onMovieListener: MovieViewHolder.OnMovieListener){
        this.items = movieList
        this.onMovieListener = onMovieListener
    }



    override fun getItemCount(): Int = items.size



    // The view Holder as requires a Listener to be passed in the constructor, to apply to each item
    class MovieViewHolder constructor(itemView: View, val onMovieListener: OnMovieListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val movie_title: TextView = itemView.movie_title_textview
        val movie_image: ImageView = itemView.movie_imageview
        fun bind(result: Result){

            movie_title.setText(result.trackName)

            // On error a placeholder movie will be loaded
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.loading_preview_image)
                .error(R.drawable.loading_preview_image)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(result.artworkUrl100?.let { formatUrl(it) })
                .into(movie_image)
            itemView.setOnClickListener(this)

        }

        // the onclick listener will return the position of the item to the calling listener to execute an action
        override fun onClick(p0: View?) {
            onMovieListener.onMovieClick(adapterPosition)
        }
        // THIS INTERFACE IS REQUIRED TO BE IMPLEMENTED WITH THE RECYCLER VIEW IN ITS CALLING ACTIVITY
        // then pass the interface to the Adapter, then to the Viewholder to complete implementation
        interface OnMovieListener{
            fun onMovieClick(position: Int)
        }

        // Formats urls to return a higher resolution image
        fun formatUrl(imageUrl: String): String{
            return imageUrl.replace("100x100bb", "500x500bb")
        }




    }


}