package com.example.mynotes.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.saveBtnColor

@Composable
fun SingleImagePreviewComposableLandscape(modifier: Modifier = Modifier, photo: Photo) {
    Surface(modifier = modifier.fillMaxSize(), color = saveBtnColor) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                contentScale = ContentScale.Fit,
                modifier = modifier
                    .size(450.dp)
                    .padding(top = 30.dp, bottom = 70.dp),
                model = photo.uri,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleImagePreviewLandscape() {
    SingleImagePreviewComposableLandscape(photo = Photo())
}