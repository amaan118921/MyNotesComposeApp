package com.example.mynotes.model

import android.os.Parcelable
import com.example.mynotes.room.NoteEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(
    var id: Int = 0,
    var title: String? = null,
    var body: String? = null,
    var timestamp: Long? = null,
    var time: String? = null,
    var date: String? = null,
    var isEdited: Boolean = false,
    var photoList: List<Photo>? = null
) : Comparable<Any>, Parcelable {

    override fun compareTo(other: Any): Int {

        val note = other as NoteModel
        note.timestamp?.let { second ->
            timestamp?.let { first ->
                if (first > second) return -1
                return 1
            }
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NoteModel) {
            return false
        }

        return other.id == id
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }
}

fun NoteModel.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id,
        title ?: "",
        body ?: "",
        timestamp ?: 0L,
        time = time ?: "",
        date = date ?: "",
        isEdited = isEdited,
        photoList = photoList?: emptyList()
    )
}
