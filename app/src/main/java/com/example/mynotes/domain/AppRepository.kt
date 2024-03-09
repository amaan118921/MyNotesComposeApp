package com.example.mynotes.domain

import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.TrashEntity
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun fetchAllNotes(): Flow<List<NoteEntity>>
    suspend fun createNote(
        noteEntity: NoteEntity
    )

    suspend fun updateNote(
        noteEntity: NoteEntity
    )

    suspend fun deleteNote(
        noteEntity: NoteEntity
    )

    suspend fun fetchNoteWithId(
        id:Int
    ): NoteEntity

    fun fetchNotesWithQuery(
        query: String
    ): Flow<List<NoteEntity>>
}