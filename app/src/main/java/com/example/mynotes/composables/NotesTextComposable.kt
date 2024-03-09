package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun NotesTextComposable(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onDrawerIconClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier
                .clickable {
                    onRefresh()
                },
            text = "Notes", style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Black
            )
        )
        Icon(
            modifier = modifier
                .size(28.dp)
                .clickable {
                    onDrawerIconClick()
                },
            tint = MaterialTheme.colorScheme.secondary,
            painter = painterResource(id = R.drawable.menu__1_),
            contentDescription = null
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NotesTextComposablePreview() {
    NotesTextComposable(onDrawerIconClick = {})
}
