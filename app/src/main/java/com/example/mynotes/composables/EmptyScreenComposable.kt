package com.example.mynotes.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.ui.theme.notesTextBodyColor

@Composable
fun EmptyScreenComposable(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    isFromTrash: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = if (!isFromTrash) R.drawable.notes else R.drawable.trash_bin__1_),
            contentDescription = null
        )
        Text(
            text = stringResource(id = if (!isFromTrash) R.string.no_notes_found else R.string.empty_trash),
            modifier = modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            color = notesTextBodyColor,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyScreenComposablePreview() {
    EmptyScreenComposable(paddingValues = PaddingValues())
}