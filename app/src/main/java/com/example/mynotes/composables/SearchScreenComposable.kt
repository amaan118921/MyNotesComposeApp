package com.example.mynotes.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.ResultState
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.toNoteModel
import java.util.Collections

@Composable
fun SearchScreenComposable(
    modifier: Modifier = Modifier,
    notesList: ResultState<List<NoteEntity>>?,
    showProgressBar: Boolean = false,
    updateProgressBar: (Boolean) -> Unit,
    navigateToEditNote: (NoteModel?) -> Unit,
    onClick: () -> Unit,
    onSearch: (String) -> Unit,
    enabled: Boolean = false,
    onBack: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
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
        SearchBarComposable(onSearchClick = {
            onSearch(it)
        }, focused = true, onClick = onClick, enabled = enabled, onBack = onBack)
        if (showProgressBar) {
            LoadingScreenComposable()
        } else {
            if (notesList?.data?.isEmpty() == true) {
                EmptyScreenComposable()
            } else NotesLazyComposable(
                list = getNotesList(notesList?.data) ?: emptyList(),
                onItemClick = {
                    navigateToEditNote(it)
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenComposablePreview() {
    SearchScreenComposable(
        notesList = ResultState.Loading(),
        updateProgressBar = {},
        navigateToEditNote = {},
        onClick = {},
        onSearch = {},
        onBack = {}
    )
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

