package com.app.func.features.room_database_sqlite_mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import com.app.func.features.room_database_sqlite_mvvm.utils.Utils.subscribeOnBackground

class NoteRepository(context: Context) {

    private val database = NoteDatabase.getInstance(context.applicationContext)
    private var noteDao: NoteDao?= database?.noteDao()
    private var allNotes: LiveData<List<Note>>?= noteDao?.getAllNotes()

    init {
        noteDao = database?.noteDao()
        allNotes = noteDao?.getAllNotes()
    }

    fun insert(note: Note) {
//        Single.just(noteDao.insert(note))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
        subscribeOnBackground {
            noteDao?.insert(note)
        }
    }

    fun update(note: Note) {
        subscribeOnBackground {
            noteDao?.update(note)
        }
    }

    fun delete(note: Note) {
        subscribeOnBackground {
            noteDao?.delete(note)
        }
    }

    fun deleteAllNotes() {
        subscribeOnBackground {
            noteDao?.deleteAllNotes()
        }
    }

    fun getAllNotes(): LiveData<List<Note>>? {
        return allNotes
    }
}