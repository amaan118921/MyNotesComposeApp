package com.example.mynotes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynotes.model.NoteModel

@Entity("notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var body: String,
    @ColumnInfo
    var timestamp: Long,
    @ColumnInfo
    var time: String,
    @ColumnInfo
    var date: String,
    @ColumnInfo
    var isEdited: Boolean = false

) {
    override fun equals(other: Any?): Boolean {
        if (other !is NoteEntity) {
            return false
        }

        return other.id == id || other.body == body
                || other.timestamp == timestamp
                || other.time == time || other.date == date || other.title == title || other.isEdited == isEdited
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + isEdited.hashCode()
        return result
    }
}


fun NoteEntity.toNoteModel(): NoteModel {
    return NoteModel(
        id, title, body, time = time, date = date, timestamp = timestamp, isEdited = isEdited
    )
}