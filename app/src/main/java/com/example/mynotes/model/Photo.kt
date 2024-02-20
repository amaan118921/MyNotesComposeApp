package com.example.mynotes.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    var uri: String? = null,
    var isPortrait: Boolean = true,
    var id: Int? = 0
): Parcelable