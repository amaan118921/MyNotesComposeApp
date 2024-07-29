package com.example.mynotes.data

import com.example.mynotes.Constants.Companion.event
import com.example.mynotes.domain.AppRepository
import com.example.mynotes.eventbus.DaoEvent
import com.example.mynotes.eventbus.Event
import com.example.mynotes.eventbus.EventBusEnum
import com.example.mynotes.room.NotesDao
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.TrashEntity
import kotlinx.coroutines.flow.Flow
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(private val dao: NotesDao) : AppRepository {
    override fun fetchAllNotes(): Flow<List<NoteEntity>> {
        event.post(
            DaoEvent(EventBusEnum.POST_NOTIFICATION, "fetchAllNotes")
        )
        return dao.fetchAllNotes()
    }

    override suspend fun createNote(noteEntity: NoteEntity) {
        event.post(
            DaoEvent(EventBusEnum.POST_NOTIFICATION, "createNote")
        )
        dao.createNote(noteEntity)
    }

    override suspend fun updateNote(noteEntity: NoteEntity) {
        event.post(
            DaoEvent(EventBusEnum.POST_NOTIFICATION, "updateNote")
        )
        dao.updateNote(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        event.post(
            DaoEvent(EventBusEnum.POST_NOTIFICATION, "deleteNote")
        )
        dao.deleteNote(noteEntity)
    }

    override suspend fun fetchNoteWithId(id: Int): NoteEntity {
        event.post(
            DaoEvent(EventBusEnum.POST_NOTIFICATION, "fetchNoteWithId")
        )
        return dao.fetchNoteWithId(id)
    }

    override fun fetchNotesWithQuery(query: String): Flow<List<NoteEntity>> {
        event.post(
            DaoEvent(EventBusEnum.POST_NOTIFICATION, "fetchNotesWithQuery")
        )
        return dao.fetchNotesWithQuery(query)
    }
}