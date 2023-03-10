package com.app.func.view.recycler_view_custom.ravi_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.func.R
import com.app.func.view.recycler_view_custom.ravi_recyclerview.CartListAdapter.MyViewHolder
import com.bumptech.glide.Glide

class CartListAdapter : RecyclerView.Adapter<MyViewHolder>() {
    private var cartList = mutableListOf<ItemCart>()

    fun setDataAdapter(list: List<ItemCart>) {
        cartList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var description: TextView
        var price: TextView
        var thumbnail: ImageView
        var viewBackground: RelativeLayout
        @JvmField
        var viewForeground: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            description = view.findViewById(R.id.description)
            price = view.findViewById(R.id.price)
            thumbnail = view.findViewById(R.id.thumbnail)
            viewBackground = view.findViewById(R.id.view_background)
            viewForeground = view.findViewById(R.id.view_foreground)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (_, name, description, price, thumbnail) = cartList!![position]
        holder.name.text = name
        holder.description.text = description
        holder.price.text = "₹$price"
        Glide.with(holder.itemView.context).load(thumbnail).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun removeItem(position: Int) {
        cartList.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(item: ItemCart, position: Int) {
        cartList.add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }
}