package com.example.mynotes.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynotes.room.converters.PhotoConverter

@Database(entities = [NoteEntity::class, TrashEntity::class], version = 105)
@TypeConverters(PhotoConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
    abstract fun getTrashDao(): TrashDao
}