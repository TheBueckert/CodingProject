package com.clyde.codingchallenge.RecyclerRelatedClasses

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// This class allows for padding to be applied to items at runtime. Applying them in the xml file had
// Some weird results on certain devices.

// 4 parameters are required for the each side of the view
class ItemSpacingDecoration(private val padding_left: Int,
                            private val padding_right: Int,
                            private val padding_top: Int,
                            private val padding_bottom: Int): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = padding_left
        outRect.right = padding_right
        outRect.top = padding_top
        outRect.bottom = padding_bottom
    }
}