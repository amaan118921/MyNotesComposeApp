package com.example.mynotes.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.BaseViewModel
import com.example.mynotes.Constants.Companion.TAG
import com.example.mynotes.ResultState
import com.example.mynotes.domain.AddToTrashUseCase
import com.example.mynotes.domain.CreateNoteUseCase
import com.example.mynotes.domain.DeleteNoteUseCase
import com.example.mynotes.domain.FetchNotesWithQueryUseCase
import com.example.mynotes.domain.UpdateNoteUseCase
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.model.toNoteEntity
import com.example.mynotes.model.toTrashEntity
import com.example.mynotes.room.NoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
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
    private var _notesLiveData: MutableLiveData<ResultState<List<NoteEntity>>>? = null
    fun getNotesLiveData(): LiveData<ResultState<List<NoteEntity>>>? = _notesLiveData


    fun isLiveDataInitialized() = _notesLiveData != null
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

    fun fetchNotesWithQuery(query: String) {
        if (!isLiveDataInitialized()) _notesLiveData = MutableLiveData()
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                fetchNotesWithQueryUseCase(query).collectLatest {
                    _notesLiveData?.value = it
                }
            } catch (e: Exception) {
                Log.e(TAG, "invoke: $e")
            }
        }
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