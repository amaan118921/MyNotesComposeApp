package com.example.mynotes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mynotes.composables.CreateNoteComposable
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.ImagePreviewComposable
import com.example.mynotes.composables.MainScreenComposable
import com.example.mynotes.compose.Screen
import com.example.mynotes.helper.CameraApi
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.utils.getUriEncoded
import com.example.mynotes.viewModel.MainScreenViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()
    private var searchJob: Job? = null
    private var note: NoteModel? = null
    private var launcher: ActivityResultLauncher<Uri>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var currentUri: Uri? = null
    private var file: File? = null
    var width: Int = 0

    @Inject
    lateinit var compressUtils: CompressUtils

    @Inject
    lateinit var cameraApi: CameraApi
    private fun popBackStack(navController: NavHostController, route: String) {
        navController.popBackStack(route = route, inclusive = false, saveState = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme {
                width = LocalConfiguration.current.screenWidthDp
                var showProgressBar by rememberSaveable {
                    mutableStateOf(false)
                }
                if (!viewModel.isLiveDataInitialized()) viewModel.fetchNotesWithQuery("")
                val notesList =
                    viewModel.getNotesLiveData()?.observeAsState()?.value
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                    composable(Screen.MainScreen.route) {
                        MainScreenComposable(
                            progressBar = showProgressBar,
                            updateProgressBar = {
                                showProgressBar = it
                            },
                            onSearch = {},
                            notesList = notesList,
                            navigateToCreateNote = {
                                navController.navigate(
                                    Screen.CreateNote.createRoute()
                                )
                            },
                            navigateToEditNote = { note ->
                                this@MainActivity.note = note
                                val noteString = getUriEncoded(note?: NoteModel())
                                navController.navigate(
                                    Screen.EditNote.createRoute(noteString)
                                )
                            },
                            onClick = { navigateToSearchScreen() },
                            onRefresh = { viewModel.fetchNotesWithQuery("") }
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
                                    route = Screen.MainScreen.route
                                )
                            },
                            onUpdateNoteClick = {
                                viewModel.updateNote(it)
                                popBackStack(
                                    navController = navController,
                                    route = Screen.MainScreen.route
                                )
                            },
                            onNoteDelete = {
                                viewModel.deleteNote(it)
                                popBackStack(
                                    navController = navController,
                                    route = Screen.MainScreen.route
                                )
                            },
                            note = note ?: NoteModel(),
                            toast = {
                                showToast(it)
                            },
                            onCameraClick = { openCamera() },
                            viewModel = viewModel,
                            onPhotoPreview = { photo, index ->
                                navController.navigate(
                                    Screen.PreviewScreen.createRoute(false, index ?: 0)
                                )
                            }, onGalleryClick = {
                                openGallery()
                            },
                            onRemovePhoto = {}
                        )
                    }
                    composable(
                        Screen.CreateNote.route
                    ) {
                        CreateNoteComposable(onBackPressed = {
                            popBackStack(
                                navController,
                                Screen.MainScreen.route
                            )
                        }, onCreateNoteClick = {
                            viewModel.createNote(it)
                            popBackStack(navController = navController, Screen.MainScreen.route)
                            if (it.body.isNullOrEmpty() && it.title.isNullOrEmpty()) {
                                Looper.myLooper()?.let { looper ->
                                    Handler(looper).postDelayed({
                                        viewModel.deleteNote(it)
                                        showToast("Empty note discarded")
                                    }, 500)
                                }
                            }
                        }, onCameraClick = {
                            openCamera()
                        }, viewModel = viewModel,
                            onImagePreview = { photo, index ->
                                navController.navigate(
                                    Screen.PreviewScreen.createRoute(
                                        true,
                                        index ?: 0
                                    )
                                )
                            }, onGalleryClick = { openGallery() },
                            onRemovePhoto = {}
                        )
                    }
                }
            }
        }
    }

    private fun openGallery() {
        galleryLauncher?.launch("image/*")
    }

    override fun onStart() {
        super.onStart()
        registerLauncher()
    }

    private fun registerLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                val compressedFile = compressUtils.getCompressedPath(file, width)
                val compressedUri = cameraApi.contentUriFromFile(compressedFile)
                viewModel.addPhoto(
                    Photo(
                        uri = currentUri.toString(),
                        isPortrait = compressUtils.isPortrait(file)
                    )
                )
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

    private fun openCamera() {
        file = cameraApi.getOutputMediaFile(CameraApi.MEDIA_TYPE_IMAGE)
        currentUri = cameraApi.contentUriFromFile(file = file)
        launcher?.launch(currentUri)
    }

    private fun navigateToSearchScreen() {
        Intent(this, SearchScreenActivity::class.java).apply {
            startActivity(this)
        }
    }

    companion object {
        fun getRandomInt(): Int {
            return (1..100).random()
        }
    }
}

