package com.example.mynotes.data

import com.example.mynotes.domain.AppRepository
import com.example.mynotes.room.NotesDao
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.TrashEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(private val dao: NotesDao) : AppRepository {
    override fun fetchAllNotes(): Flow<List<NoteEntity>> {
        return dao.fetchAllNotes()
    }

    override suspend fun createNote(noteEntity: NoteEntity) {
        dao.createNote(noteEntity)
    }

    override suspend fun updateNote(noteEntity: NoteEntity) {
        dao.updateNote(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        dao.deleteNote(noteEntity)
    }

    override suspend fun fetchNoteWithId(id: Int): NoteEntity {
        return dao.fetchNoteWithId(id)
    }

    override fun fetchNotesWithQuery(query: String): Flow<List<NoteEntity>> {
        return dao.fetchNotesWithQuery(query)
    }
}