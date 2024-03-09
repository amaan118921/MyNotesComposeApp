package com.example.mynotes.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.ui.theme.antiFlashWhite
import com.example.mynotes.ui.theme.notesTextColorLight

@Composable
fun NotesBottomItem(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    imageVector: ImageVector? = null,
    painterId: Int,
    tint: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(40.dp)),
        color = color
    ) {
        IconButton(onClick = { onClick() }) {
            if (imageVector == null) {
                Icon(
                    painter = painterResource(id = painterId),
                    contentDescription = null,
                    tint = tint
                )
            } else {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = tint
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotesBottomItemPreview() {
    NotesBottomItem(painterId = 0, imageVector = Icons.Default.List) {

    }
}