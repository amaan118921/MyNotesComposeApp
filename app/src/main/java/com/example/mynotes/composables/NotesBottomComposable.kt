package com.example.mynotes.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.ui.theme.notesTextColor
import com.example.mynotes.ui.theme.saveBtnColor

@Composable
fun NotesBottomComposable(
    modifier: Modifier = Modifier,
    onCreateNote: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    imageVector: ImageVector,
    isFromTrash: Boolean = false
) {
    Surface(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp)),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            modifier = modifier
                .padding(7.dp), horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            NotesBottomItem(painterId = R.drawable.baseline_camera_alt_24) {
                onCameraClick()
            }
            NotesBottomItem(painterId = R.drawable.baseline_insert_photo_24) {
                onGalleryClick()
            }
            NotesBottomItem(painterId = R.drawable.baseline_attach_file_24) {

            }
            NotesBottomItem(
                imageVector = if (isFromTrash) null else imageVector,
                color = MaterialTheme.colorScheme.surface,
                tint = MaterialTheme.colorScheme.secondary,
                painterId = if (isFromTrash) R.drawable.baseline_restore_24 else 0
            ) {
                onCreateNote()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotesBottomComposablePreview(modifier: Modifier = Modifier) {
    NotesBottomComposable(
        onCreateNote = {},
        onCameraClick = {},
        imageVector = Icons.Default.Done,
        onGalleryClick = {})
}