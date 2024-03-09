package com.example.mynotes.composables

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.ui.theme.fabColor
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun FabComposable(modifier: Modifier = Modifier, onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = onFabClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "add",
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Preview
@Composable
fun FabComposablePreview() {
    FabComposable() {

    }
}