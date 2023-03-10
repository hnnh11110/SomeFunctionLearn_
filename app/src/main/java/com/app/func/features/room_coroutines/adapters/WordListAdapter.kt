package com.app.func.features.room_coroutines.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.app.func.databinding.ItemNoteBinding
import com.app.func.features.room_coroutines.Word

private val diffCallback = object : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.word == newItem.word
    }
}

class WordListAdapter(private val onItemClickListener: (Word) -> Unit) :
    ListAdapter<Word, WordListAdapter.WordHolder>(diffCallback) {

    inner class WordHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindDataToViews(word: Word) {
            binding.textViewTitle.text = word.word
        }

        init {
            itemView.setOnClickListener {
                if (adapterPosition != NO_POSITION)
                    onItemClickListener(getItem(adapterPosition))
            }
        }
    }

    fun getNoteAt(position: Int): Word = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return WordHolder(binding)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        with(getItem(position)) {
            holder.bindDataToViews(this)
        }
    }
}