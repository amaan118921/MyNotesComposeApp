package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun NotesTextComposable(modifier: Modifier = Modifier, onRefresh: () -> Unit = {}) {
    Text(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp)
            .clickable {
                onRefresh()
            },
        text = "Notes", style = MaterialTheme.typography.headlineLarge.copy(
            color = notesTextColor, fontWeight = FontWeight.Black
        )
    )
}


@Preview(showBackground = true)
@Composable
fun NotesTextComposablePreview() {
    NotesTextComposable()
}
