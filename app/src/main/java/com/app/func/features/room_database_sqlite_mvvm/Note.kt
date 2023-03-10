package com.app.func.features.room_database_sqlite_mvvm

import androidx.room.Entity
import androidx.room.PrimaryKey

//https://github.com/Caagnuur/ToDoList
@Entity(tableName = "note_table")
data class Note(
    val title: String,
    val description: String,
    val priority: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
