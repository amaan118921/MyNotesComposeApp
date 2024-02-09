package com.example.mynotes.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 101)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): AppDao
}