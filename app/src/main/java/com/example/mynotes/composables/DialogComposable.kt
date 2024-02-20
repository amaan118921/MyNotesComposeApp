package com.example.mynotes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun DialogComposable(modifier: Modifier = Modifier, onDismiss: () -> Unit, onDelete: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties()) {
        Card(
            modifier = modifier
                .padding(20.dp), colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = modifier.padding(20.dp)) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier.weight(1f),
                        text = "Delete Note?",
                        color = notesTextColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default
                    )
                    Icon(modifier = modifier.clickable {
                        onDismiss()
                    }, imageVector = Icons.Rounded.Close, contentDescription = null)
                }
                Text(
                    modifier = modifier.padding(top = 12.dp),
                    text = "Are you sure, you want to delete this note?",
                    color = notesTextColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                ) {
                    Button(
                        modifier = modifier
                            .weight(0.5f)
                            .padding(end = 12.dp),
                        onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(
                            containerColor = notesTextColor
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        modifier = modifier
                            .weight(0.5f)
                            .padding(start = 12.dp),
                        onClick = { onDelete() }, colors = ButtonDefaults.buttonColors(
                            containerColor = notesTextColor
                        )
                    ) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialogComposablePreview() {
    DialogComposable(onDismiss = {}, onDelete = {})
}