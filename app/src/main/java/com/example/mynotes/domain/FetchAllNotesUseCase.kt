package com.example.mynotes.domain

import com.example.mynotes.ResultState
import com.example.mynotes.room.NoteEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchAllNotesUseCase @Inject constructor(private val appRepository: AppRepository) {
//    operator fun invoke(): Flow<ResultState<List<NoteEntity>>> = flow {
//        emit(ResultState.Loading())
//        val data = appRepository.fetchAllNotes()
//        delay(2500)
//        emit(ResultState.Success(data))
//    }
}