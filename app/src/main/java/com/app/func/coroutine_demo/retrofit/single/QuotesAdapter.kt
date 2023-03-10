package com.app.func.coroutine_demo.retrofit.single

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.func.coroutine_demo.data.model.Result
import com.app.func.databinding.ItemLayoutApiQuoteBinding

class QuotesAdapter : RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    var listResult: List<Result> = listOf()

    fun setData(listResult: List<Result>) {
        this.listResult = listResult
        notifyDataSetChanged()
    }


    inner class QuoteViewHolder(val binding: ItemLayoutApiQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(position: Int) {
            binding.textViewAuthor.text = listResult[position].author
            binding.textViewContent.text = listResult[position].content
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutApiQuoteBinding.inflate(inflater, parent, false)
        return QuoteViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bindViews(position)
    }

    override fun getItemCount(): Int = listResult.size
}