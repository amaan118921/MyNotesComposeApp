package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.ResultState
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.toNoteModel
import com.example.mynotes.ui.theme.notesTextColor
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
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier
                    .clickable {
                    },
                text = "Search", style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Black
                )
            )
        }
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
                },
                showBottomSheet = {noteModel, i ->  },
                selectedItemIdx = -1
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

