package com.example.mynotes.data

import com.example.mynotes.domain.TrashRepository
import com.example.mynotes.room.TrashDao
import com.example.mynotes.room.TrashEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrashRepositoryImpl @Inject constructor(
    private val trashDao: TrashDao
) : TrashRepository {
    override fun fetchNotesFromTrash(query: String): Flow<List<TrashEntity>> {
        return trashDao.fetchNotesWithQueryFromTrash(query)
    }

    override suspend fun addToTrash(trashEntity: TrashEntity) {
        trashDao.addNoteToTrash(trashEntity)
    }

    override suspend fun deleteFromTrash(trashEntity: TrashEntity) {
        trashDao.deleteNote(trashEntity)
    }
}