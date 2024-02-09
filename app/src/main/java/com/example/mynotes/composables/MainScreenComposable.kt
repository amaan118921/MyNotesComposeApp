package com.example.mynotes.composables

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mynotes.ResultState
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.toNoteModel
import java.util.Collections

@Composable
fun MainScreenComposable(
    modifier: Modifier = Modifier,
    progressBar: Boolean,
    updateProgressBar: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    notesList: ResultState<List<NoteEntity>>?,
    navigateToCreateNote: () -> Unit,
    navigateToEditNote: (NoteModel?) -> Unit,
    onClick: () -> Unit,
    onRefresh: () -> Unit
) {
    Surface {
        when (notesList) {
            is ResultState.Success -> {
                updateProgressBar(false)
            }

            is ResultState.Loading -> {
                updateProgressBar(true)
            }

            else -> {
                updateProgressBar(false)
            }
        }
        HomeScreenComposable(onSearchClick = {
            onSearch(it)
        }, list = getNotesList(notesList?.data) ?: emptyList(), onFabClick = {
            navigateToCreateNote()
        }, onItemClick = {
            navigateToEditNote(it)
        }, showProgressBar = progressBar, enabled = false, onClick = onClick, onRefresh = onRefresh)
    }
}

private fun getNotesList(notesList: List<NoteEntity>?): List<NoteModel>? {
    val list = notesList?.map {
        it.toNoteModel()
    }
    sortList(list)
    return list
}

private fun sortList(list: List<NoteModel>?) {
    list?.let { Collections.sort(it) }
}

