package com.example.mynotes.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.ResultState
import com.example.mynotes.enums.BottomSheetItems
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.room.toNoteModel
import kotlinx.coroutines.launch
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
    onRefresh: () -> Unit,
    onBottomSheetAction: (BottomSheetItems, NoteModel) -> Unit,
    onNoteDelete: () -> Unit,
    onTrashClick: () -> Unit,
    onDarkModeClick: (Boolean) -> Unit,
    onPolicyClick: () -> Unit,
    darkMode: Boolean,
    drawerState: DrawerState
) {

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Red
    ) {
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
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
                ModalDrawerSheet {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = "Notes", style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.secondary
                                ), modifier = modifier.padding(start = 16.dp, top = 16.dp)
                            )
                        }
                        Column(modifier = modifier.padding(top = 18.dp)) {
                            DrawerItemComposable(
                                id = R.drawable.baseline_delete_24,
                                text = "Trash",
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onTrashClick()
                                }
                            )
                            DrawerItemComposableWithSwitch(
                                id = R.drawable.baseline_dark_mode_24,
                                text = "Dark Mode",
                                paddingValues = NavigationDrawerItemDefaults.ItemPadding,
                                isChecked = darkMode,
                                modifier = modifier.padding(top = 2.dp),
                                onDarkModeClick = {
                                    onDarkModeClick(it)
                                }
                            )
                            DrawerItemComposable(
                                modifier = modifier.padding(top = 2.dp),
                                id = R.drawable.baseline_lock_24,
                                text = "Privacy Policy",
                                onClick = onPolicyClick
                            )
                        }
                    }
                }
            }) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    HomeScreenComposable(
                        onSearchClick = {
                            onSearch(it)
                        },
                        list = getNotesList(notesList?.data) ?: emptyList(),
                        onFabClick = {
                            navigateToCreateNote()
                        },
                        onItemClick = {
                            navigateToEditNote(it)
                        },
                        showProgressBar = progressBar,
                        enabled = false,
                        onClick = onClick,
                        onRefresh = onRefresh,
                        onBottomSheetAction = onBottomSheetAction,
                        onNoteDelete = onNoteDelete,
                        onDrawerClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
            }
        }

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

