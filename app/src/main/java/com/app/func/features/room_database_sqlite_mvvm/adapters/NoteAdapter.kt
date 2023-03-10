package com.app.func.features.room_database_sqlite_mvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.app.func.databinding.ItemNoteBinding
import com.app.func.features.room_database_sqlite_mvvm.Note

private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.id == newItem.id
    }
}

class NoteAdapter(private val onItemClickListener: (Note) -> Unit) :
    ListAdapter<Note, NoteAdapter.NoteHolder>(diffCallback) {

    inner class NoteHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        val tvTitle = binding.textViewTitle
//        val tvDescription = binding.textViewDescription
//        val tvPriority = binding.textViewPriority

        fun bindDataToViews(note: Note) {
            binding.textViewTitle.text = "${note.id} - ${note.title}"
            binding.textViewDescription.text = note.description
            binding.textViewPriority.text = note.priority.toString()
        }

        init {
            itemView.setOnClickListener {
                if (adapterPosition != NO_POSITION)
                    onItemClickListener(getItem(adapterPosition))
            }
        }
    }

    fun getNoteAt(position: Int): Note = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return NoteHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        with(getItem(position)) {
//            holder.tvTitle.text = title
//            holder.tvDescription.text = description
//            holder.tvPriority.text = priority.toString()
            holder.bindDataToViews(this)
        }
    }
}