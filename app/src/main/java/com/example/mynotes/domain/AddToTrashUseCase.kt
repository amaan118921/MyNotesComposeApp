package com.example.mynotes.domain

import com.example.mynotes.room.TrashEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToTrashUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {
    suspend operator fun invoke(trashEntity: TrashEntity) {
        trashRepository.addToTrash(trashEntity)
    }
}