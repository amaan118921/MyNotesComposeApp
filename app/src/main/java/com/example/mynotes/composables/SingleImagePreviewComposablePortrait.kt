package com.example.mynotes.composables

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.saveBtnColor
import net.engawapg.lib.zoomable.ZoomState
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SingleImagePreviewComposablePortrait(
    modifier: Modifier = Modifier,
    photo: Photo,
    zoomState: ZoomState
) {
    Surface(modifier = modifier, color = saveBtnColor) {
        GlideImage(
            contentScale = ContentScale.Fit,
            modifier = modifier
                .zoomable(zoomState = zoomState)
                .fillMaxSize()
                .padding(top = 30.dp, bottom = 70.dp),
            model = Uri.parse(photo.uri),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SingleImagePreview() {
    SingleImagePreviewComposablePortrait(photo = Photo(), zoomState = ZoomState())
}