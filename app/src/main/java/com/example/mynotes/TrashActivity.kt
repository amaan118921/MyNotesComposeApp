package com.example.mynotes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.ImagePreviewComposable
import com.example.mynotes.composables.TrashScreenComposable
import com.example.mynotes.compose.Screen
import com.example.mynotes.enums.Theme
import com.example.mynotes.model.NoteModel
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.utils.Preferences
import com.example.mynotes.utils.getUriEncoded
import com.example.mynotes.viewModel.TrashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrashActivity : BaseActivity() {
    private val viewModel by viewModels<TrashViewModel>()
    private var searchJob: Job? = null

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode by rememberSaveable {
                mutableStateOf(isDarkMode())
            }
            MyNotesTheme(darkTheme = darkMode) {
                if (!viewModel.isLiveDataInitialized()) viewModel.fetchNotesFromTrash("")
                val trashList = viewModel.getTrashLiveData()?.observeAsState()?.value
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TrashScreen.route
                    ) {
                        composable(Screen.TrashScreen.route) {
                            TrashScreenComposable(
                                enabled = true,
                                notesList = trashList,
                                navigateToEditNote = {
                                    val noteString = getUriEncoded(it)
                                    viewModel.setAll(it?.photoList ?: emptyList())
                                    viewModel.setPhotos(viewModel.photoList)
                                    navController.navigate(Screen.EditNote.createRoute(noteString))
                                },
                                onClick = {},
                                onSearch = {
                                    searchJob?.cancel(null)
                                    searchJob = lifecycleScope.launch {
                                        delay(500)
                                        viewModel.fetchNotesFromTrash(it.trim())
                                    }
                                }
                            ) {
                                finish()
                            }
                        }
                        composable(
                            Screen.EditNote.route,
                            arguments = Screen.EditNote.navArguments
                        ) {
                            val note = it.arguments?.getParcelable<NoteModel>(Constants.NOTE)
                            EditNoteComposable(
                                isFromTrash = true,
                                onBackPressed = {
                                    popBackStack(
                                        navController = navController,
                                        route = Screen.TrashScreen.route
                                    )
                                },
                                onUpdateNoteClick = {},
                                onNoteDelete = {
                                    viewModel.deleteNoteFromTrash(it)
                                    popBackStack(
                                        navController, Screen.TrashScreen.route
                                    )
                                },
                                toast = {
                                    showToast(it)
                                },
                                onCameraClick = {
                                    showToast("Can't edit in trash")
                                },
                                viewModel = viewModel,
                                onPhotoPreview = { photo, index ->
                                    navController.navigate(
                                        Screen.PreviewScreen.createRoute(
                                            false, index ?: 0
                                        )
                                    )
                                }, onGalleryClick = { showToast("Can't edit in trash") },
                                onRemovePhoto = {},
                                note = note ?: NoteModel(),
                                onRestoreClick = {
                                    restoreNote(it)
                                    popBackStack(
                                        navController, Screen.TrashScreen.route
                                    )
                                }
                            )
                        }
                        composable(
                            Screen.PreviewScreen.route,
                            arguments = Screen.PreviewScreen.navArguments
                        ) {
                            val fromCreate = it.arguments?.getBoolean(Constants.FROM_CREATE)
                            val initPos = it.arguments?.getInt(Constants.POS)
                            ImagePreviewComposable(
                                onBack = { create ->
                                    popBackStack(
                                        navController = navController,
                                        route = if (create) Screen.CreateNote.route else Screen.EditNote.route
                                    )
                                },
                                isFromCreate = fromCreate ?: true,
                                initPos = initPos,
                                viewModel = viewModel,
                                onRemove = {
                                    viewModel.removePhoto(it)
                                    viewModel.setPhotos(viewModel.photoList)
                                },
                                isFromTrash = true,
                                toast = {
                                    showToast(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun restoreNote(it: NoteModel) {
        viewModel.createNote(it)
        viewModel.deleteNoteFromTrash(it)
        showToast(it = getString(R.string.restore_success))
    }

    private fun isDarkMode(): Boolean {
        val dark = preferences.getSharedPreferencesInt(Constants.DARK)
        return dark == Theme.DARK.value
    }

    private fun popBackStack(navController: NavHostController, route: String) {
        navController.popBackStack(route = route, inclusive = false, saveState = true)
    }
}
