package com.example.mynotes.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.model.NoteModel

@Composable
fun HomeScreenComposable(
    modifier: Modifier = Modifier,
    onSearchClick: (String) -> Unit,
    list: List<NoteModel>,
    onFabClick: () -> Unit,
    onItemClick: (NoteModel?) -> Unit,
    showProgressBar: Boolean = true,
    enabled: Boolean,
    onRefresh: () -> Unit,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        NotesTextComposable(onRefresh = onRefresh)
        Scaffold(
            topBar = {
                SearchBarComposable(onSearchClick = onSearchClick, enabled = enabled, onClick = onClick, onBack = {})
            },
            floatingActionButton = {
                FabComposable {
                    onFabClick()
                }
            }
        ) { innerPadding ->
            if (showProgressBar) {
                LoadingScreenComposable()
            } else {
                if (list.isEmpty()) {
                    EmptyScreenComposable(paddingValues = innerPadding)
                } else NotesLazyComposable(
                    list = list,
                    paddingValues = innerPadding,
                    onItemClick = onItemClick
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenComposablePreview() {
    HomeScreenComposable(
        onSearchClick = {},
        list = emptyList(),
        onFabClick = {},
        onItemClick = {},
        enabled = true,
        onClick = {},
        onRefresh = {}
    )
}
