package com.app.func

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.func.databinding.SimpleSelectableListItemBinding

class LocationAdapter(
    private val dataSet: List<Location>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private var selectedLocation: Location? = null
    var currentItemClicked: ((position : Int) -> Unit)? = null

    var selectedLocationIndex: Int
        get() = dataSet.indexOf(selectedLocation)
        set(index) {
            this.selectedLocation = dataSet[index]
        }

    interface OnItemClickListener {
        fun onItemClick(location: Location)
    }

    class ViewHolder(binding: SimpleSelectableListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var title = binding.titleCountry
//        var title: TextView = v.findViewById<View>(android.R.id.text1) as TextView

        fun bindViews(location: Location) {
            title.text = location.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SimpleSelectableListItemBinding =
            SimpleSelectableListItemBinding.inflate(layoutInflater, parent, false)
//        val view =
//            layoutInflater.inflate(android.R.layout.simple_selectable_list_item, parent, false)
//
//        return ViewHolder(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = dataSet[position]
//        holder.title.text = location.name
        holder.bindViews(location)

        holder.itemView.setOnClickListener {
            listener.onItemClick(location)
            selectedLocation = location
            currentItemClicked?.invoke(position)
            notifyDataSetChanged()
        }


        if (dataSet[position] == selectedLocation) {
            val backgroundColor =
                ContextCompat.getColor(holder.itemView.context, R.color.color_01BCD4)
            holder.itemView.setBackgroundColor(backgroundColor)
            holder.title.setTextColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.title.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount() = dataSet.size
}
