package com.example.mynotes.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.enums.BottomSheetItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheetComposable(
    modifier: Modifier = Modifier,
    hideSheet: () -> Unit,
    onClick: (BottomSheetItems) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(dragHandle = {}, onDismissRequest = { hideSheet() }, sheetState = sheetState) {
        Column {
            BottomSheetItemComposable(item = BottomSheetItems.SEND) {
                onClick(BottomSheetItems.SEND)
            }
            BottomSheetItemComposable(item = BottomSheetItems.COPY) {
                onClick(BottomSheetItems.COPY)
            }
            BottomSheetItemComposable(item = BottomSheetItems.DELETE) {
                onClick(BottomSheetItems.DELETE)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBottomSheetComposablePreview() {
    HomeBottomSheetComposable(hideSheet = {}, onClick = {})
}