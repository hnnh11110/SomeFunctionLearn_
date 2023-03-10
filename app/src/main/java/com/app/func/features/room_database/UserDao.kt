package com.app.func.features.room_database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    fun suspendInsertUser(user: User)

    @Insert
    fun insertUser(user: User)

    @Query("select * from user")
    fun getUsers(): List<User>

    @Query("select * from user order by userName asc")
    fun getUsersWithAsc(): Flow<List<User>>

    @Query("select * from user order by userName desc")
    fun getUsersWithDesc(): List<User>

    @Query("SELECT * FROM user WHERE userId = :id")
    fun getUserById(id: Int?): User?

    //    @Query("SELECT * FROM user WHERE userName LIKE :name")
    @Query("SELECT * FROM user WHERE userName IN (:name)")
    fun getUserByName(name: String?): User?

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

//    @Query("delete from user")
//    suspend fun deleteAllUser()

}