package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity
import com.example.mynotes.ui.theme.antiFlashWhite
import com.example.mynotes.ui.theme.notesTextBodyColor
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun NoteItemComposable(
    modifier: Modifier = Modifier,
    note: NoteModel? = null,
    onItemClick: (NoteModel?) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onItemClick(note)
            }, color = antiFlashWhite
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    text = note?.title ?: "Title", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = notesTextColor,
                        fontFamily = FontFamily.Default
                    )
                )
                Text(
                    modifier = modifier.padding(end = 16.dp),
                    text = note?.time ?: "8:30 pm",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Light, fontSize = 14.sp, color = notesTextColor
                    )
                )
            }
            Text(
                text = note?.body ?: stringResource(id = R.string.lorem),
                modifier = modifier.padding(start = 16.dp, top = 7.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal, color = notesTextBodyColor
                )
            )
            Row {
                Text(
                    text = note?.date ?: "3 Feb 2024",
                    modifier = modifier
                        .padding(start = 16.dp, top = 15.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light, color = notesTextColor, fontSize = 14.sp
                    )
                )
                Text(
                    text = if (note?.isEdited == true) stringResource(id = R.string.edited) else "",
                    modifier = modifier.padding(start = 16.dp, top = 15.dp, end = 16.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold, color = notesTextColor, fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItemComposablePreview() {
    NoteItemComposable(onItemClick = {})
}