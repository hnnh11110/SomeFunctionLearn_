package com.app.func.features.room_database_sqlite_mvvm.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

object Utils {

    fun subscribeOnBackground(function: () -> Unit) {
        Single.fromCallable {
            function()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}