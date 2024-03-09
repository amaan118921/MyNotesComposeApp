package com.example.mynotes.model

import android.os.Parcelable
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.TrashEntity
import com.example.mynotes.utils.getDate
import com.example.mynotes.utils.getTime
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class NoteModel(
    var id: Int = 0,
    var title: String? = null,
    var body: String? = null,
    var timestamp: Long? = null,
    var time: String? = null,
    var date: String? = null,
    var isEdited: Boolean = false,
    var photoList: List<Photo>? = null,
) : Comparable<Any>, Parcelable {

    fun deepCopy(): NoteModel {
        return NoteModel(
            title = this.title,
            body = this.body,
            timestamp = Calendar.getInstance().timeInMillis,
            time = getTime(),
            date = getDate(),
            isEdited = false,
            photoList = this.photoList
        )
    }

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
        photoList = photoList ?: emptyList()
    )
}

fun NoteModel.toTrashEntity(): TrashEntity {
    return TrashEntity(
        id,
        title ?: "",
        body ?: "",
        timestamp ?: 0L,
        time = time ?: "",
        date = date ?: "",
        isEdited = isEdited,
        photoList = photoList ?: emptyList()
    )
}