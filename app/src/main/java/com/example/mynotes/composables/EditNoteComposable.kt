package com.example.mynotes.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mynotes.BaseViewModel
import com.example.mynotes.R
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.room.toNoteModel
import com.example.mynotes.ui.theme.notesTextColor
import com.example.mynotes.ui.theme.notesTextColorLight
import com.example.mynotes.utils.getDate
import com.example.mynotes.utils.getTime
import com.example.mynotes.viewModel.MainScreenViewModel
import java.util.Calendar

@Composable
fun EditNoteComposable(
    modifier: Modifier = Modifier,
    isFromTrash: Boolean = false,
    onBackPressed: () -> Unit,
    onUpdateNoteClick: (NoteModel) -> Unit,
    onNoteDelete: (NoteModel) -> Unit,
    toast: (String) -> Unit,
    note: NoteModel,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    viewModel: BaseViewModel? = null,
    onRestoreClick: ((NoteModel) -> Unit)? = null,
    onPhotoPreview: (Photo?, Int?) -> Unit,
    onRemovePhoto: (Photo) -> Unit
) {
    val photoList = viewModel?.getPhotoListState()
    var titleState by remember {
        mutableStateOf(note.title ?: "")
    }
    var bodyState by remember {
        mutableStateOf(note.body ?: "")
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = modifier.weight(1f)) {
            Row(
                modifier = modifier
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonComposable(imageVector = Icons.Default.KeyboardArrowLeft) {
                    onBackPressed()
                }
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = modifier
                            .size(45.dp)
                            .clickable {
                                showDialog = true
                            }, border = BorderStroke(1.dp, notesTextColorLight)
                    ) {
                        Column(
                            modifier = modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.secondary,
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                modifier = modifier.size(25.dp)
                            )
                        }
                    }
                    SaveButtonComposable(text = stringResource(id = if (isFromTrash) R.string.restore else R.string.update)) {
                        if (isFromTrash) {
                            onRestoreClick?.invoke(note)
                            return@SaveButtonComposable
                        }
                        note.apply {
                            if (title != titleState || body != bodyState || this.photoList?.equals(
                                    photoList
                                ) == false
                            ) {
                                val updatedNote =
                                    updateNote(
                                        titleState,
                                        bodyState,
                                        this,
                                        photoList ?: emptyList(),
                                        toast
                                    )
                                onUpdateNoteClick(updatedNote)

                            } else toast("make changes to update note...")
                        }
                    }
                }
            }
            TextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = titleState, onValueChange = {
                    titleState = it
                }, colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor =  MaterialTheme.colorScheme.surface
                ), textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Black, fontSize = 20.sp
                ), placeholder = {
                    Text(
                        text = "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
            Text(
                modifier = modifier.padding(start = 16.dp),
                text = note.date ?: "",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            TextField(
                modifier = modifier
                    .fillMaxWidth(),
                value = bodyState, onValueChange = {
                    bodyState = it
                }, colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ), textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Normal, fontSize = 17.sp
                ), placeholder = {
                    Text(
                        text = "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
            if (!photoList.isNullOrEmpty()) PhotoLazyComposable(
                photoList = photoList,
                onClick = onPhotoPreview,
                onRemovePhoto = onRemovePhoto
            )
        }
        NotesBottomComposable(onCreateNote = {
            if (isFromTrash) {
                onRestoreClick?.invoke(note)
                return@NotesBottomComposable
            }
            note.apply {
                if (title != titleState || body != bodyState || this.photoList?.equals(
                        photoList
                    ) == false
                ) {
                    val updatedNote =
                        updateNote(
                            titleState,
                            bodyState,
                            this,
                            photoList ?: emptyList(),
                            toast
                        )
                    onUpdateNoteClick(updatedNote)

                } else toast("make changes to update note...")
            }

        }, onCameraClick = {
            onCameraClick()
        }, imageVector = Icons.Default.Done, onGalleryClick = {
            onGalleryClick()
        }, isFromTrash = isFromTrash)
    }
    if (showDialog) {
        DialogComposable(onDismiss = { showDialog = false }, isFromTrash = isFromTrash) {
            note.let {
                onNoteDelete(it)
            }
            showDialog = false
        }
    }
}

fun updateNote(
    titleState: String,
    bodyState: String,
    note: NoteModel,
    photoList: List<Photo>,
    toast: (String) -> Unit
): NoteModel {
    note.apply {
        title = titleState.trim()
        body = bodyState.trim()
        timestamp = Calendar.getInstance().timeInMillis
        date = getDate()
        time = getTime()
        this.photoList = photoList
        isEdited = true
        return this
    }
}

@Preview(showBackground = true)
@Composable
fun EditNoteComposablePreview() {
    EditNoteComposable(
        onBackPressed = {},
        onUpdateNoteClick = {},
        onNoteDelete = {},
        toast = {},
        onCameraClick = {},
        onPhotoPreview = { photo, i -> },
        onGalleryClick = {},
        onRemovePhoto = {},
        note = NoteModel(title = "Title", body = "Note", date = "Feb 13, 2024"),
        onRestoreClick = {}
    )
}