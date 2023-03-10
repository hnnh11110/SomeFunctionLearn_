package com.app.func.features.room_database

//https://github.com/velmurugan-murugesan/Android-Example
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Int? = null,
    @ColumnInfo(name = "userName")
    val userName: String,
    @ColumnInfo(name = "location")
    var location: String,
    @ColumnInfo(name = "email")
    val email: String
)