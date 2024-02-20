package com.example.mynotes.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheetComposable(modifier: Modifier = Modifier) {
    ModalBottomSheet(onDismissRequest = { }) {
        
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBottomSheetComposablePreview() {
    HomeBottomSheetComposable()
}