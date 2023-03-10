package com.app.func.features.room_database

import android.content.Context
import android.os.Handler
import android.os.Looper

class UserRepository(context: Context) {

    //https://developer.android.com/codelabs/android-room-with-a-view-kotlin#6
    var dao: UserDao? = AppDatabase.newInstance(context)?.userDao()
    val handler: Handler = Handler(Looper.getMainLooper())

    //Fetch All the Users
    fun getAllUsers(): List<User>? {
        //return dao?.getUsers()
        //return dao?.getUsersWithAsc()
        return dao?.getUsersWithDesc()
    }

    // Insert new user
    fun insertUser(user: User) {
        handler.post {
            dao?.insertUser(user)
        }
    }

    fun suspendInsertUser(user: User) {
        dao?.suspendInsertUser(user)
    }

    fun updateUser(user: User) {
        dao?.updateUser(user)
    }

    fun deleteUser(users: User) {
        dao?.deleteUser(users)
    }


}