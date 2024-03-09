package com.example.mynotes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.notesTextColor
import com.example.mynotes.utils.getDate
import com.example.mynotes.utils.getTime
import com.example.mynotes.viewModel.MainScreenViewModel
import java.util.Calendar

@Composable
fun CreateNoteComposable(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onCreateNoteClick: (NoteModel) -> Unit,
    onCameraClick: () -> Unit,
    viewModel: MainScreenViewModel? = null,
    onImagePreview: (Photo?, Int?) -> Unit,
    onGalleryClick: () -> Unit,
    onRemovePhoto: (Photo) -> Unit
) {
    var clearPhotos by rememberSaveable {
        mutableStateOf(true)
    }
    var titleState by rememberSaveable {
        mutableStateOf("")
    }
    var bodyState by rememberSaveable {
        mutableStateOf("")
    }

    if (clearPhotos) {
        viewModel?.clearPhotos()
        clearPhotos = false
    }

    val photosList = viewModel?.getPhotoListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface), horizontalAlignment = Alignment.CenterHorizontally
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
                SaveButtonComposable(text = stringResource(id = R.string.save)) {
                    val note = createNote(titleState, bodyState, photosList)
                    onCreateNoteClick(note)
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
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ), textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Black, fontSize = 20.sp
                ), placeholder = {
                    Text(
                        text = "Title",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
            Text(
                modifier = modifier.padding(start = 16.dp),
                text = getDate(),
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
                        text = "Note",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
            if (!photosList.isNullOrEmpty()) PhotoLazyComposable(
                photoList = photosList,
                onClick = onImagePreview,
                onRemovePhoto = onRemovePhoto
            )
        }
        NotesBottomComposable(onCreateNote = {
            val note = createNote(titleState, bodyState, photosList)
            onCreateNoteClick(note)
        }, onCameraClick = {
            onCameraClick()
        }, imageVector = Icons.Default.Add, onGalleryClick = onGalleryClick)
    }
}

fun createNote(title: String, body: String, photosList: List<Photo>?): NoteModel {
    return NoteModel(
        title = title.trim(),
        body = body.trim(),
        time = getTime(),
        date = getDate(),
        id = MainActivity.getRandomInt(),
        timestamp = Calendar.getInstance().timeInMillis,
        photoList = photosList
    )
}

@Preview(showBackground = true)
@Composable
fun CreateNoteComposablePreview() {
    CreateNoteComposable(
        onBackPressed = {},
        onCreateNoteClick = {},
        onCameraClick = {},
        onImagePreview = {photo, i ->  },
        onGalleryClick = {}, onRemovePhoto = {})
}