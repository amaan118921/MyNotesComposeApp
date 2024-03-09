package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.enums.BottomSheetItems
import com.example.mynotes.ui.theme.saveBtnColor

@Composable
fun BottomSheetItemComposable(
    modifier: Modifier = Modifier,
    item: BottomSheetItems,
    onClick: () -> Unit
) {
    Surface(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        }
        .padding()) {
        Column(
            modifier = modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(id = item.textId),
                    modifier = modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = modifier.size(24.dp),
                    imageVector = item.imageVector,
                    contentDescription = null
                )
            }
            if (item != BottomSheetItems.DELETE) {
                Divider(
                    modifier = modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = com.example.mynotes.ui.theme.Divider
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetItemComposablePreview() {
    BottomSheetItemComposable(item = BottomSheetItems.SEND, onClick = {})
}