package com.example.mynotes.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.ui.theme.Pink40
import com.example.mynotes.ui.theme.notesTextBodyColor
import com.example.mynotes.ui.theme.notesTextColor
import com.example.mynotes.ui.theme.saveBtnColor

@Composable
fun DrawerItemComposableWithSwitch(
    modifier: Modifier = Modifier,
    id: Int,
    text: String,
    isChecked: Boolean,
    paddingValues: PaddingValues,
    onDarkModeClick: (Boolean) -> Unit,
) {
    Surface(modifier = modifier.padding(paddingValues)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.secondary,
                painter = painterResource(id = id),
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .padding(start = 10.dp)
                    .weight(1f),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Switch(
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = modifier.padding(end = 10.dp),
                checked = isChecked,
                onCheckedChange = { onDarkModeClick(isChecked) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerItemComposableWithSwitchPreview() {
    DrawerItemComposableWithSwitch(
        id = R.drawable.baseline_dark_mode_24,
        text = "Dark Mode",
        isChecked = false,
        paddingValues = NavigationDrawerItemDefaults.ItemPadding,
        onDarkModeClick = {}
    )
}