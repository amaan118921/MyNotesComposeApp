package com.example.mynotes.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.notesTextColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemComposable(
    modifier: Modifier = Modifier,
    photo: Photo,
    onClick: (Photo?) -> Unit,
    onRemovePhoto: (Photo) -> Unit,
    showRemove: Boolean = false,
    isLast: Boolean
) {
    Card(
        modifier = modifier
            .padding(start = 14.dp, top = 16.dp, end = if(isLast) 14.dp else 0.dp)
            .clickable {
                onClick(photo)
            }, elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        GlideImage(
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(150.dp),
            model = photo.uri, contentDescription = null,
        )
    }
}

fun formatUri(uri: String?): String? {
    if (uri?.contains(".jpg") == true) return uri
    uri?.let {
        return "$it.jpg"
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun PhotoItemComposablePreview() {
    PhotoItemComposable(photo = Photo(), onClick = {}, onRemovePhoto = {}, isLast = true)
}