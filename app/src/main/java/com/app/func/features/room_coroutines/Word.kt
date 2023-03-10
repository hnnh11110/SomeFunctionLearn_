package com.app.func.features.room_coroutines

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin
    https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0
 */

@Entity(tableName = "word_table")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "word")
    val word: String
)
