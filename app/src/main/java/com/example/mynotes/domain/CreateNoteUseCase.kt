package com.example.mynotes.domain

import androidx.lifecycle.LiveData
import com.example.mynotes.room.NoteEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNoteUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(noteEntity: NoteEntity) {
        return appRepository.createNote(noteEntity)
    }
}