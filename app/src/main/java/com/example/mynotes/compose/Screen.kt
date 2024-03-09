package com.example.mynotes.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mynotes.Constants.Companion.FROM_CREATE
import com.example.mynotes.Constants.Companion.NOTE
import com.example.mynotes.Constants.Companion.POS
import com.example.mynotes.navtypes.NoteParamType

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {

    data object MainScreen : Screen(
        "mainComposable"
    ) {
        fun createRoute() = "mainComposable"
    }

    data object PreviewScreen : Screen(
        "previewComposable/{$FROM_CREATE}/{$POS}",
        navArguments = listOf(
            navArgument(FROM_CREATE) {
                type = NavType.BoolType
            }, navArgument(POS) {
                type = NavType.IntType
            }
        )
    ) {
        fun createRoute(fromCreate: Boolean, pos: Int) =
            "previewComposable/$fromCreate/$pos"
    }

    data object CreateNote : Screen(
        "createNoteComposable"
    ) {
        fun createRoute() = "createNoteComposable"
    }

    data object EditNote : Screen(
        "editNoteComposable/{$NOTE}",
        navArguments = listOf(
            navArgument(NOTE) {
                type = NoteParamType()
            }
        )
    ) {
        fun createRoute(note: String) = "editNoteComposable/${note}"
    }

    data object SearchScreen : Screen(
        route = "searchScreenComposable"
    ) {
        fun createRoute() = "searchScreenComposable"
    }

    data object TrashScreen : Screen(
        route = "trashScreenComposable"
    ) {
        fun createRoute() = "trashScreenComposable"
    }

}