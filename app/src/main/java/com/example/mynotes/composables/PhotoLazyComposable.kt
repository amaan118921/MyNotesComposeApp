package com.example.mynotes.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun PhotoLazyComposable(
    modifier: Modifier = Modifier,
    photoList: List<Photo>,
    onClick: (Photo?, Int?) -> Unit,
    onRemovePhoto: (Photo) -> Unit
) {
    Column {
        Text(
            text = "Photos",
            modifier = modifier.padding(top = 16.dp, start = 16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary
        )
        LazyRow(modifier = modifier.padding(top = 8.dp)) {
            itemsIndexed(items = photoList) { index, item ->
                PhotoItemComposable(photo = item, onClick = {
                    onClick(it, index)
                }, onRemovePhoto = onRemovePhoto, isLast = index == photoList.lastIndex)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoLazyComposablePreview() {
    PhotoLazyComposable(photoList = emptyList(), onClick = { photo, i -> }, onRemovePhoto = {})
}