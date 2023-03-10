package com.app.func.features.room_database_sqlite_mvvm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.func.features.room_database_sqlite_mvvm.utils.Utils.subscribeOnBackground

@Database(entities = arrayOf(Note::class), version = 100, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { populateDatabase(it) }
            }
        }

        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, NoteDatabase::class.java, "note_database").build()
                //.fallbackToDestructiveMigration().addCallback(roomCallback).build()
            }
            return instance
        }

        private fun populateDatabase(db: NoteDatabase) {
            val noteDao = db.noteDao()
            subscribeOnBackground {
            noteDao.insert(Note("title 1", "desc 1", 1))
            noteDao.insert(Note("title 2", "desc 2", 2))
            noteDao.insert(Note("title 3", "desc 3", 3))

            }
        }
    }


}