package com.example.mynotes.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynotes.BaseViewModel
import com.example.mynotes.ResultState
import com.example.mynotes.domain.CreateNoteUseCase
import com.example.mynotes.domain.DeleteNoteFromTrashUseCase
import com.example.mynotes.domain.FetchNotesFromTrashUseCase
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.toNoteEntity
import com.example.mynotes.model.toTrashEntity
import com.example.mynotes.room.TrashEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val fetchNotesFromTrashUseCase: FetchNotesFromTrashUseCase,
    private val deleteNoteFromTrashUseCase: DeleteNoteFromTrashUseCase
) : BaseViewModel() {
    private var _trashLiveData: MutableLiveData<ResultState<List<TrashEntity>>>? = null

    fun getTrashLiveData(): LiveData<ResultState<List<TrashEntity>>>? = _trashLiveData

    fun isLiveDataInitialized() = _trashLiveData != null

    fun fetchNotesFromTrash(query: String) {
        if (!isLiveDataInitialized()) _trashLiveData = MutableLiveData()
        viewModelScope.launch {
            fetchNotesFromTrashUseCase(query).collectLatest {
                _trashLiveData?.value = it
            }
        }
    }

    fun deleteNoteFromTrash(noteModel: NoteModel) {
        viewModelScope.launch {
            deleteNoteFromTrashUseCase(
                noteModel.toTrashEntity()
            )
        }
    }

    fun createNote(noteModel: NoteModel) {
        viewModelScope.launch {
            createNoteUseCase(
                noteModel.toNoteEntity()
            )
        }
    }
}