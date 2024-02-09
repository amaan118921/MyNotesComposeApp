package com.example.mynotes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                Text(
                    text = "Are you sure, you want to delete this note?",
                    color = notesTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(
                            containerColor = notesTextColor
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
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