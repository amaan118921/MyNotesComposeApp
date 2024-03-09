package com.example.mynotes.domain

import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.TrashEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteNoteFromTrashUseCase @Inject constructor(private val trashRepository: TrashRepository) {
    suspend operator fun invoke(trashEntity: TrashEntity) {
        return trashRepository.deleteFromTrash(trashEntity)
    }
}