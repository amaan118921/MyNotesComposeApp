package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.composables.CreateNoteComposable
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.MainScreenComposable
import com.example.mynotes.model.NoteModel
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.viewModel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()
    private var searchJob: Job? = null
    private var note: NoteModel? = null
    private fun popBackStack(navController: NavHostController, route: String) {
        navController.popBackStack(route = route, inclusive = false, saveState = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme {
                var showProgressBar by rememberSaveable {
                    mutableStateOf(false)
                }
                if (!viewModel.isLiveDataInitialized()) viewModel.fetchNotesWithQuery("")
                val notesList =
                    viewModel.getNotesLiveData()?.observeAsState()?.value
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Constants.MAINSCREEN) {
                    composable(Constants.MAINSCREEN) {
                        MainScreenComposable(
                            progressBar = showProgressBar,
                            updateProgressBar = {
                                showProgressBar = it
                            },
                            onSearch = {},
                            notesList = notesList,
                            navigateToCreateNote = { navController.navigate(Constants.CREATENOTES) },
                            navigateToEditNote = { note ->
                                this@MainActivity.note = note
                                navController.navigate(Constants.EDITNOTES)
                            },
                            onClick = { navigateToSearchScreen() },
                            onRefresh = {viewModel.fetchNotesWithQuery("")}
                        )
                    }
                    composable(
                        Constants.EDITNOTES
                    ) {
                        EditNoteComposable(
                            onBackPressed = {
                                popBackStack(
                                    navController = navController,
                                    route = Constants.MAINSCREEN
                                )
                            },
                            onUpdateNoteClick = {
                                viewModel.updateNote(it)
                                popBackStack(
                                    navController = navController,
                                    route = Constants.MAINSCREEN
                                )
                            },
                            onNoteDelete = {
                                viewModel.deleteNote(it)
                                popBackStack(
                                    navController = navController,
                                    route = Constants.MAINSCREEN
                                )
                            },
                            toast = {
                                showToast(it)
                            },
                            note = note ?: NoteModel()
                        )
                    }
                    composable(Constants.CREATENOTES) {
                        CreateNoteComposable(onBackPressed = {
                            popBackStack(
                                navController,
                                Constants.MAINSCREEN
                            )
                        }, onCreateNoteClick = {
                            viewModel.createNote(it)
                            popBackStack(navController = navController, Constants.MAINSCREEN)
                        })
                    }
                }
            }
        }
    }

    private fun navigateToSearchScreen() {
        Intent(this, SearchScreenActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun showToast(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun getRandomInt(): Int {
            return (1..100).random()
        }
    }
}

