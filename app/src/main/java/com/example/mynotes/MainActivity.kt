package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.composables.CreateNoteComposable
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.ImagePreviewComposable
import com.example.mynotes.composables.MainScreenComposable
import com.example.mynotes.compose.Screen
import com.example.mynotes.enums.BottomSheetItems
import com.example.mynotes.enums.Theme
import com.example.mynotes.helper.CameraApi
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.utils.Preferences
import com.example.mynotes.utils.getNoteIntentString
import com.example.mynotes.utils.getUriEncoded
import com.example.mynotes.viewModel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()
    private var note: NoteModel? = null
    private var launcher: ActivityResultLauncher<Uri>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var currentUri: Uri? = null
    private var file: File? = null
    var width: Int = 0

    @Inject
    lateinit var compressUtils: CompressUtils

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var cameraApi: CameraApi
    private fun popBackStack(navController: NavHostController, route: String) {
        navController.popBackStack(route = route, inclusive = false, saveState = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkMode by rememberSaveable {
                mutableStateOf(isDarkMode())
            }
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            onBackPressedDispatcher.addCallback(this) {
                if (drawerState.isOpen) {
                    scope.launch {
                        drawerState.close()
                    }
                } else finish()
            }
            viewModel.toastLiveData().observe(this) {
                if (it.isNullOrEmpty()) return@observe
                showToast(it)
            }
            MyNotesTheme(darkTheme = darkMode) {
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
                        var note by remember {
                            mutableStateOf<NoteModel?>(null)
                        }
                        Surface(modifier = Modifier.fillMaxSize(), color = Color.Red) {
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
                                    val noteString = getUriEncoded(note ?: NoteModel())
                                    viewModel.setAll(note?.photoList ?: emptyList())
                                    viewModel.setPhotos(viewModel.photoList)
                                    navController.navigate(
                                        Screen.EditNote.createRoute(noteString)
                                    )
                                },
                                onClick = { navigateToSearchScreen() },
                                onRefresh = { viewModel.fetchNotesWithQuery("") },
                                onBottomSheetAction = { bottomSheetItems, noteModel ->
                                    when (bottomSheetItems) {
                                        BottomSheetItems.SEND -> {
                                            shareNote(noteModel)
                                        }

                                        BottomSheetItems.DELETE -> {
                                            note = noteModel
                                        }

                                        BottomSheetItems.COPY -> {
                                            createCopy(noteModel)
                                        }
                                    }
                                },
                                onNoteDelete = {
                                    note?.let {
                                        viewModel.deleteNote(it)
                                        viewModel.addToTrash(it)
                                    }
                                }, onDarkModeClick = {
                                    updateTheme(update = {
                                        darkMode = !it
                                    }, it)
                                },
                                onPolicyClick = {},
                                onTrashClick = {
                                    navToTrashActivity()
                                },
                                darkMode = darkMode,
                                drawerState = drawerState
                            )
                        }
                    }
                    composable(
                        Screen.PreviewScreen.route,
                        arguments = Screen.PreviewScreen.navArguments
                    ) {
                        val fromCreate = it.arguments?.getBoolean(Constants.FROM_CREATE)
                        val initPos = it.arguments?.getInt(Constants.POS)
                        val note = it.arguments?.getParcelable<NoteModel>(Constants.NOTE)
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
                            }
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
                                viewModel.addToTrash(it)
                                popBackStack(
                                    navController = navController,
                                    route = Screen.MainScreen.route
                                )
                            },
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
                            onRemovePhoto = {},
                            note = note ?: NoteModel()
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
                            if (it.body.isNullOrEmpty() && it.title.isNullOrEmpty() && it.photoList.isNullOrEmpty()) {
                                Looper.myLooper()?.let { looper ->
                                    Handler(looper).postDelayed({
                                        viewModel.deleteNote(it)
                                        viewModel.addToTrash(it)
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

    private fun navToTrashActivity() {
        Intent(this, TrashActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun createCopy(noteModel: NoteModel) {
        noteModel.deepCopy().apply {
            viewModel.createNote(this)
        }
    }

    private fun shareNote(noteModel: NoteModel?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getNoteIntentString(noteModel))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_note))
        startActivity(shareIntent)
    }

    private fun openGallery() {
        galleryLauncher?.launch("image/*")
    }

    override fun onStart() {
        super.onStart()
        registerLauncher()
    }

    @SuppressLint("WrongConstant")
    private fun registerLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                val compressedFile = compressUtils.getCompressedPath(file, width)
                val compressedUri = cameraApi.contentUriFromFile(compressedFile)
                savePhoto(currentUri)
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            it?.forEach { uri ->
                savePhoto(uri)
            }
        }
    }

    private fun savePhoto(uri: Uri?) {
        val photo = Photo(
            uri = uri.toString(),
            isPortrait = currentUri?.let {
                compressUtils.isPortrait(it)
            } ?: true,
            id = Calendar.getInstance().timeInMillis
        )
        viewModel.addPhoto(
            photo
        )
        viewModel.setPhotos(viewModel.photoList)
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

    private fun isDarkMode(): Boolean {
        val dark = preferences.getSharedPreferencesInt(Constants.DARK)
        return dark == Theme.DARK.value
    }

    private fun updateTheme(update: (Boolean) -> Unit, mode: Boolean) {
        preferences.setSharedPreferencesInt(
            Constants.DARK,
            if (mode) Theme.LIGHT.value else Theme.DARK.value
        )
        update(mode)
    }
    companion object {
        fun getRandomInt(): Int {
            return (1..100).random()
        }
    }
}

