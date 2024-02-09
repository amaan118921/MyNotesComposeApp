package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.ui.theme.antiFlashWhite
import com.example.mynotes.ui.theme.notesTextColor

@Composable
fun SearchBarComposable(
    modifier: Modifier = Modifier,
    onSearchClick: (String) -> Unit,
    focused: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {

    var text by rememberSaveable {
        mutableStateOf("")
    }
    val focusRequester = remember {
        FocusRequester()
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .focusRequester(focusRequester)
            .clickable {
                onClick()
            },
        color = antiFlashWhite
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (enabled) IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector =
                    Icons.Default.ArrowBack, contentDescription = "back"
                )
            }
            TextField(
                enabled = enabled,
                singleLine = true,
                value = TextFieldValue(
                    text = text, selection = TextRange(text.length)
                ),
                onValueChange = {
                    text = it.text
                    onSearchClick(text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (text.isNotEmpty()) {
                                text = ""
                                onSearchClick(text)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (text.isEmpty()) Icons.Default.Search else Icons.Default.Close,
                            contentDescription = "Search"
                        )
                    }
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily.Default, fontSize = 16.sp
                ),
                placeholder = {
                    Text(text = "Search")
                },
                colors = TextFieldDefaults.colors(
                    disabledPlaceholderColor = notesTextColor,
                    disabledTrailingIconColor = notesTextColor,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = antiFlashWhite,
                    focusedContainerColor = antiFlashWhite,
                    unfocusedContainerColor = antiFlashWhite,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )

            )
            if (focused) {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun SearchBarComposablePreview() {
    SearchBarComposable(onSearchClick = {}, onClick = {}, onBack = {})
}