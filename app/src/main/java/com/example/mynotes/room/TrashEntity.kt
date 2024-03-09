package com.example.mynotes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.room.converters.RoomConverter

@Entity("trash")
data class TrashEntity(
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
    var isEdited: Boolean = false,
    @ColumnInfo
    @TypeConverters(RoomConverter::class)
    var photoList: List<Photo>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is NoteEntity) {
            return false
        }

        return id == other.id

    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + isEdited.hashCode()
        result = 31 * result + photoList.hashCode()
        return result
    }
}

fun TrashEntity.toNoteModel(): NoteModel {
    return NoteModel(
        id, title, body, timestamp, time, date, isEdited, photoList
    )
}

