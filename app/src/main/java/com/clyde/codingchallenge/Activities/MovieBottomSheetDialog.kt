package com.clyde.codingchallenge.Activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.clyde.codingchallenge.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*

// I figured a bottom sheet modal dialog would have easy animations, be lightweight and have interesting animations, closer to the original design...
class MovieBottomSheetDialog : BottomSheetDialogFragment() {


    // creation of the view takes default parameters from the bundle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.bottom_sheet_layout, container, false)


        v.title_textview.setText(arguments?.getString("title"))
        v.genre_textview.setText(arguments?.getString("genre"))
        v.actors_names_textview.setText(arguments?.getString("cast"))
        v.description_textview.setText(arguments?.getString("description"))
        v.mock_display_home_button.setOnClickListener { dismiss() }


        Glide.with(v.context)
            .load(formatUrl(arguments?.getString("img_url")))
            .into(v.movie_imageview)


        return v
    }
    // this onStart was overriden to allow the modal to cover the full screen
    // due to trouble with the kotlin parameters, I translated some java code to kotlin from stack overflow
    //https://stackoverflow.com/questions/36030879/bottomsheetdialogfragment-how-to-set-expanded-height-or-min-top-offset
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog

        if (dialog!=null){
            val bottomSheet: View = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        val view = view

        view?.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight

        }

    }
    // this will format a url to retrieve a higher resolution image from the server
    fun formatUrl(imageUrl: String?): String{
        if (imageUrl != null) {
            return imageUrl.replace("100x100bb", "500x500bb")
        }
        else return ""//todo
    }
}