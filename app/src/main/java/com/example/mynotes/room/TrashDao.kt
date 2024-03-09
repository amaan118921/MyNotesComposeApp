package com.example.mynotes.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteToTrash(trashEntity: TrashEntity)

    @Delete
    suspend fun deleteNote(trashEntity: TrashEntity)

    @Query("SELECT * from trash where title Like '%' || :query || '%'")
    fun fetchNotesWithQueryFromTrash(query: String): Flow<List<TrashEntity>>

    @Query("DELETE from trash")
    suspend fun clearTrash()
}