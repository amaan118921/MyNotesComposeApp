package com.example.mynotes.domain

import com.example.mynotes.ResultState
import com.example.mynotes.room.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchNotesWithIdUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(id: Int): NoteEntity {
        return appRepository.fetchNoteWithId(id)
    }
}