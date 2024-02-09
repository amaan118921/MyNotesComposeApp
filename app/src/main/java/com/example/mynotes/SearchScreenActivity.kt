package com.example.mynotes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.SearchScreenComposable
import com.example.mynotes.model.NoteModel
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchScreenActivity : ComponentActivity() {
    private val viewModel by viewModels<SearchViewModel>()
    private var note: NoteModel? = null
    private var searchJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme {
                var showProgressBar by remember {
                    mutableStateOf(false)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (!viewModel.isLiveDataInitialized()) viewModel.fetchNotesWithQuery("")
                    val notesList =
                        viewModel.getNotesLiveData()?.observeAsState()?.value
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Constants.SEARCH) {
                        composable(Constants.SEARCH) {
                            SearchScreenComposable(
                                notesList = notesList,
                                showProgressBar = showProgressBar,
                                updateProgressBar = {
                                    showProgressBar = it
                                },
                                navigateToEditNote = { note ->
                                    this@SearchScreenActivity.note = note
                                    navController.navigate(Constants.EDITNOTES)
                                },
                                onClick = {},
                                enabled = true,
                                onSearch = {
                                    searchJob?.cancel(null)
                                    searchJob = lifecycleScope.launch(Dispatchers.Default) {
                                        delay(500)
                                        viewModel.fetchNotesWithQuery(it.trim())
                                    }
                                },
                                onBack = { finish() }
                            )
                        }
                        composable(
                            Constants.EDITNOTES
                        ) {
                            EditNoteComposable(
                                onBackPressed = {
                                    popBackStack(
                                        navController = navController,
                                        route = Constants.SEARCH
                                    )
                                },
                                onUpdateNoteClick = {
                                    viewModel.updateNote(it)
                                    finish()
                                },
                                onNoteDelete = {
                                    viewModel.deleteNote(it)
                                    finish()
                                },
                                toast = {
                                    showToast(it)
                                },
                                note = note ?: NoteModel()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun popBackStack(navController: NavHostController, route: String) {
        navController.popBackStack(route = route, inclusive = false, saveState = true)
    }

    private fun showToast(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}
