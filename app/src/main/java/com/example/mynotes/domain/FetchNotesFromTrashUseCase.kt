package com.example.mynotes.domain

import com.example.mynotes.ResultState
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.TrashEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchNotesFromTrashUseCase @Inject constructor(private val trashRepository: TrashRepository) {
    operator fun invoke(query: String): Flow<ResultState<List<TrashEntity>>> = flow {
        emit(ResultState.Loading())
        trashRepository.fetchNotesFromTrash(query).collect {
            emit(ResultState.Success(it))
        }
    }
}