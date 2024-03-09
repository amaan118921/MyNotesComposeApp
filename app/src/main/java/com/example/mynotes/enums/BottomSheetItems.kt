package com.example.mynotes.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mynotes.R

enum class BottomSheetItems(val textId: Int, val imageVector: ImageVector) {
    SEND(
        R.string.send,
        Icons.Default.Share
    ),
    COPY(
        R.string.copy,
        Icons.Default.AddCircle
    ),
    DELETE(
        R.string.delete,
        Icons.Default.Delete
    );
}