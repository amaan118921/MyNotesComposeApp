package com.example.mynotes.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun ImagePreviewComposable(
    modifier: Modifier = Modifier,
    photoList: List<Photo>? = null,
    onBack: (Boolean) -> Unit,
    initPos: Int?,
    isFromCreate: Boolean
) {
    var position by remember {
        mutableStateOf(0)
    }
    Column {
        Surface(modifier = modifier
            .fillMaxWidth()
            .height(60.dp), color = notesTextColor) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onBack(isFromCreate) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Text(
                    modifier = modifier.padding(start = 10.dp),
                    text = "${position+1} of ${photoList?.size}",
                    color = Color.White
                )
            }
        }
        ComposePagerSnapHelper(width = LocalConfiguration.current.screenWidthDp.dp,
            onPositionChange = {
                position = it
            }, initialPosition = initPos) {
            LazyRow(state = it) {
                items(items = photoList ?: emptyList()) { photo ->
                    if(photo.isPortrait) SingleImagePreviewComposablePortrait(photo = photo)
                    else SingleImagePreviewComposableLandscape(photo = photo)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePreviewComposablePreview() {
    ImagePreviewComposable(onBack = {}, isFromCreate = false, initPos = null)
}