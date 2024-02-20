package com.example.mynotes.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynotes.room.converters.PhotoConverter

@Database(entities = [NoteEntity::class], version = 104)
@TypeConverters(PhotoConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): AppDao
}