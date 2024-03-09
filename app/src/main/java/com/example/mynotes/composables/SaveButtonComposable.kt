package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.model.NoteModel
import com.example.mynotes.ui.theme.saveBtnColor

@Composable
fun SaveButtonComposable(
    modifier: Modifier = Modifier, text: String, onSaveClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier
            .size(width = 80.dp, height = 45.dp)
            .clickable {
                onSaveClick()
            },
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier.padding(10.dp),
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SaveButtonComposablePreview() {
    SaveButtonComposable(text = "Save") {}
}