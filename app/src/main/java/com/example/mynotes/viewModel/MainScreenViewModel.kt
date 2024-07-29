package com.example.mynotes.viewModel

import androidx.lifecycle.viewModelScope
import com.example.mynotes.BaseViewModel
import com.example.mynotes.ResultState
import com.example.mynotes.domain.AddToTrashUseCase
import com.example.mynotes.domain.CreateNoteUseCase
import com.example.mynotes.domain.DeleteNoteUseCase
import com.example.mynotes.domain.FetchNotesWithQueryUseCase
import com.example.mynotes.domain.UpdateNoteUseCase
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.toNoteEntity
import com.example.mynotes.model.toTrashEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val fetchNotesWithQueryUseCase: FetchNotesWithQueryUseCase,
    private val addToTrashUseCase: AddToTrashUseCase
) :
    BaseViewModel() {

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    fun createNote(noteModel: NoteModel) {
        viewModelScope.launch {
            createNoteUseCase(
                noteModel.toNoteEntity()
            )
        }
    }

    fun deleteNote(noteModel: NoteModel) {
        viewModelScope.launch {
            deleteNoteUseCase(
                noteModel.toNoteEntity()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val listFlow = trigger.flatMapLatest {
        fetchNotesWithQueryUseCase("")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ResultState.Loading())


    fun refresh() {
        trigger.tryEmit(Unit)
    }

    fun updateNote(it: NoteModel) {
        viewModelScope.launch {
            updateNoteUseCase(
                it.toNoteEntity()
            )
        }
    }

    fun addToTrash(noteModel: NoteModel) {
        viewModelScope.launch {
            addToTrashUseCase(
                noteModel.toTrashEntity()
            )
        }
    }


}