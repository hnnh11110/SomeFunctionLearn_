package com.app.func.thread_demo.snowy.model

import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tutorial(
    val name: String,
    val url: String,
    val description: String
) : Parcelable