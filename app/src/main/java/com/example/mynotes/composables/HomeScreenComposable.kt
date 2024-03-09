package com.example.mynotes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.enums.BottomSheetItems
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
    onClick: () -> Unit,
    onBottomSheetAction: (BottomSheetItems, NoteModel) -> Unit,
    onNoteDelete: () -> Unit,
    onDrawerClick: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var selectedItemIdx by remember {
        mutableStateOf(-1)
    }
    var noteModel: NoteModel? = null
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    Surface {
        Column(modifier = modifier) {
            NotesTextComposable(onRefresh = onRefresh, onDrawerIconClick = onDrawerClick)
            Scaffold(
                topBar = {
                    SearchBarComposable(
                        onSearchClick = onSearchClick,
                        enabled = enabled,
                        onClick = onClick,
                        onBack = {})
                },
                floatingActionButton = {
                    FabComposable {
                        onFabClick()
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) { innerPadding ->
                if (showProgressBar) {
                    LoadingScreenComposable()
                } else {
                    if (list.isEmpty()) {
                        EmptyScreenComposable(paddingValues = innerPadding)
                    } else NotesLazyComposable(
                        list = list,
                        paddingValues = innerPadding,
                        onItemClick = onItemClick,
                        showBottomSheet = { note, idx ->
                            selectedItemIdx = idx
                            noteModel = note
                            showBottomSheet = true
                        }, selectedItemIdx = selectedItemIdx
                    )
                }
            }
        }
        if (showBottomSheet) {
            HomeBottomSheetComposable(hideSheet = {
                selectedItemIdx = -1
                showBottomSheet = false
            }) {
                noteModel?.let { note ->
                    onBottomSheetAction(it, note)
                }
                if (it != BottomSheetItems.DELETE) selectedItemIdx = -1
                else showDialog = true
                showBottomSheet = false
            }
        }
        if (showDialog) {
            DialogComposable(onDismiss = {
                selectedItemIdx = -1
                showDialog = false
            }) {
                selectedItemIdx = -1
                showDialog = false
                onNoteDelete()
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
        onRefresh = {},
        onBottomSheetAction = { bottomSheetItems, noteModel -> },
        onNoteDelete = {},
        onDrawerClick = {}
    )
}
