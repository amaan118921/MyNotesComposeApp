package com.example.mynotes.domain

import com.example.mynotes.room.NoteEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateNoteUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(noteEntity: NoteEntity) {
        return appRepository.createNote(noteEntity)
    }
}