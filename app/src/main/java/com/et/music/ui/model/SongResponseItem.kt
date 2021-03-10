package com.et.music.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongResponseItem(
    val id: Int,
    val duration: Int,
    val genre: String,
    val image_url: String,
    val title: String
):Parcelable