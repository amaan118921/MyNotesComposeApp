package com.example.mynotes.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity

@Composable
fun NotesLazyComposable(
    modifier: Modifier = Modifier,
    list: List<NoteModel>,
    paddingValues: PaddingValues = PaddingValues(),
    onItemClick: (NoteModel?) -> Unit
) {
    LazyColumn(modifier = modifier.padding(paddingValues)) {
        items(items = list) {
            NoteItemComposable(note = it, onItemClick = onItemClick)
        }
    }
}

@Preview
@Composable
fun NotesLazyComposablePreview() {
    NotesLazyComposable(list = emptyList(), paddingValues = PaddingValues(), onItemClick = {})
}
