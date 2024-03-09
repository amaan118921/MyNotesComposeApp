package com.example.mynotes.domain

import com.example.mynotes.room.TrashEntity
import kotlinx.coroutines.flow.Flow

interface TrashRepository {
    fun fetchNotesFromTrash(
        query: String
    ): Flow<List<TrashEntity>>

    suspend fun addToTrash(
        trashEntity: TrashEntity
    )

    suspend fun deleteFromTrash(
        trashEntity: TrashEntity
    )
}