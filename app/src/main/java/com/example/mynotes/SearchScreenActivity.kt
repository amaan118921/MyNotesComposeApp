package com.example.mynotes

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.ImagePreviewComposable
import com.example.mynotes.composables.SearchScreenComposable
import com.example.mynotes.compose.Screen
import com.example.mynotes.helper.CameraApi
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.utils.getUriEncoded
import com.example.mynotes.viewModel.SearchViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchScreenActivity : BaseActivity() {
    private val viewModel by viewModels<SearchViewModel>()
    private var searchJob: Job? = null
    private var launcher: ActivityResultLauncher<Uri>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var currentUri: Uri? = null
    private var note: NoteModel? = null

    @Inject
    lateinit var cameraApi: CameraApi
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
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SearchScreen.route
                    ) {
                        composable(Screen.SearchScreen.route) {
                            SearchScreenComposable(
                                notesList = notesList,
                                showProgressBar = showProgressBar,
                                updateProgressBar = {
                                    showProgressBar = it
                                },
                                navigateToEditNote = { note ->
                                    this@SearchScreenActivity.note = note
                                    val noteString = getUriEncoded(note?: NoteModel())
                                    navController.navigate(
                                        Screen.EditNote.createRoute(
                                            noteString
                                        )
                                    )
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
                            Screen.PreviewScreen.route,
                            arguments = Screen.PreviewScreen.navArguments
                        ) {
                            val fromCreate = it.arguments?.getBoolean(Constants.FROM_CREATE)
                            val initPos = it.arguments?.getInt(Constants.POS)
                            ImagePreviewComposable(
                                photoList = viewModel.photoList,
                                onBack = { create ->
                                    popBackStack(
                                        navController = navController,
                                        route = if (create) Screen.CreateNote.route else Screen.EditNote.route
                                    )
                                },
                                isFromCreate = fromCreate ?: true,
                                initPos = initPos
                            )
                        }
                        composable(
                            Screen.EditNote.route,
                            arguments = Screen.EditNote.navArguments
                        ) {
                            val note = it.arguments?.getParcelable<NoteModel>(Constants.NOTE)
                            EditNoteComposable(
                                onBackPressed = {
                                    popBackStack(
                                        navController = navController,
                                        route = Screen.SearchScreen.route
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
                                note = note ?: NoteModel(),
                                toast = {
                                    showToast(it)
                                },
                                onCameraClick = { openCamera() },
                                viewModel = viewModel,
                                onPhotoPreview = { photo, index ->
                                    navController.navigate(
                                        Screen.PreviewScreen.createRoute(
                                            false, index ?: 0
                                        )
                                    )
                                }, onGalleryClick = {
                                    openGallery()
                                },
                                onRemovePhoto = {}
                            )
                        }
                    }
                }
            }
        }
    }

    private fun openGallery() {
        galleryLauncher?.launch("image/*")
    }

    private fun openCamera() {
        val uri = cameraApi.getOutputMediaFileUri(CameraApi.MEDIA_TYPE_IMAGE)
        this.currentUri = uri
        launcher?.launch(currentUri)
    }

    override fun onStart() {
        super.onStart()
        registerLauncher()
    }

    private fun registerLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                viewModel.addPhoto(Photo(uri = currentUri.toString()))
                viewModel.setPhotos(viewModel.photoList)
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            it?.forEach { uri ->
                viewModel.addPhoto(Photo(uri = uri.toString()))
                viewModel.setPhotos(viewModel.photoList)
            }
        }
    }

    private fun popBackStack(navController: NavHostController, route: String) {
        navController.popBackStack(route = route, inclusive = false, saveState = true)
    }

}
