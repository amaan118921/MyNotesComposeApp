package com.example.mynotes.domain

import com.example.mynotes.ResultState
import com.example.mynotes.room.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchNotesWithQueryUseCase @Inject constructor(private val appRepository: AppRepository) {
    operator fun invoke(query: String): Flow<ResultState<List<NoteEntity>>> = flow {
        emit(ResultState.Loading())
        appRepository.fetchNotesWithQuery(query).collect {
            emit(ResultState.Success(it))
        }
    }
}