package com.example.mynotes.composables

import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.model.NoteModel
import com.example.mynotes.room.NoteEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesLazyComposable(
    modifier: Modifier = Modifier,
    list: List<NoteModel>,
    selectedItemIdx: Int,
    paddingValues: PaddingValues = PaddingValues(),
    showBottomSheet: (NoteModel, Int) -> Unit,
    onItemClick: (NoteModel?) -> Unit
) {
    LazyColumn(modifier = modifier.padding(paddingValues)) {
        itemsIndexed(items = list, key = { id, note ->
            note.id
        }) { idx, item ->
            NoteItemComposable(
                note = item,
                onItemClick = onItemClick,
                showBottomSheet = showBottomSheet,
                isSelected = selectedItemIdx == idx,
                idx = idx,
                modifier = modifier.animateItemPlacement(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun NotesLazyComposablePreview() {
    NotesLazyComposable(
        list = emptyList(), paddingValues = PaddingValues(),
        onItemClick = {},
        showBottomSheet = { noteModel, i -> },
        selectedItemIdx = -1
    )
}
