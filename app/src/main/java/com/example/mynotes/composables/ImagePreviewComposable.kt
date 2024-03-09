package com.example.mynotes.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.mynotes.BaseViewModel
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.notesTextColor
import net.engawapg.lib.zoomable.rememberZoomState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagePreviewComposable(
    modifier: Modifier = Modifier,
    onBack: (Boolean) -> Unit,
    initPos: Int?,
    viewModel: BaseViewModel?,
    isFromCreate: Boolean,
    isFromTrash: Boolean = false,
    toast: (String) -> Unit = {},
    onRemove: (Int) -> Unit
) {
    var position by remember {
        mutableStateOf(0)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val zoomState = rememberZoomState()
    val photoList = viewModel?.getPhotoListState()
    val pagerState = rememberPagerState(initPos ?: 0) { photoList?.size ?: 0 }
    if (photoList?.isEmpty() == true) {
        onBack(isFromCreate)
    }
    LaunchedEffect(position) {
        zoomState.reset()
    }
    Column {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp), color = notesTextColor
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onBack(isFromCreate) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Text(
                    modifier = modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    text = "${position + 1} of ${photoList?.size}",
                    color = Color.White
                )
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                if (expanded) {
                    DropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false },
                        offset = DpOffset(LocalConfiguration.current.screenWidthDp.dp, 0.dp)
                    ) {
                        DropdownMenuItem(text =
                        {
                            Text(text = "Delete")
                        }, onClick = {
                            if (isFromTrash) {
                                toast("Can't edit in trash...")
                                expanded = false
                                return@DropdownMenuItem
                            }
                            onRemove(position)
                            expanded = false
                        })
                    }
                }
            }
        }
        HorizontalPager(state = pagerState, pageSize = PageSize.Fill) { idx ->
            position = pagerState.currentPage
            SingleImagePreviewComposablePortrait(
                photo = photoList?.get(idx) ?: Photo(),
                zoomState = zoomState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePreviewComposablePreview() {
    ImagePreviewComposable(
        onBack = {},
        isFromCreate = false,
        initPos = null,
        viewModel = null,
        toast = {},
        isFromTrash = false,
        onRemove = {})
}