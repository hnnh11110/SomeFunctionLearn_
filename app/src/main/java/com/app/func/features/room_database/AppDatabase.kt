package com.app.func.features.room_database

import android.content.Context
import androidx.room.*

//@Database(entities = [User::class], version = 101, exportSchema = false)
@Database(entities = arrayOf(User::class), version = 101, exportSchema = false)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null

        fun newInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "user.db"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}