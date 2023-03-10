package com.app.func.view.recycler_view_custom

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {

    fun onSwipedViewToLeft(viewHolder: RecyclerView.ViewHolder)

    fun onSwipedViewToRight(viewHolder: RecyclerView.ViewHolder)

}