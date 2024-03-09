package com.example.mynotes.model

import android.os.Parcelable
import com.example.mynotes.utils.getTime
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Photo(
    var uri: String? = null,
    var isPortrait: Boolean = true,
    var id: Long = 0
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other !is Photo) return false
        return uri == other.uri
    }

    override fun hashCode(): Int {
        var result = uri?.hashCode() ?: 0
        result = 31 * result + isPortrait.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

}