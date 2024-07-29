package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.Constants.Companion.NOTE
import com.example.mynotes.composables.CreateNoteComposable
import com.example.mynotes.composables.EditNoteComposable
import com.example.mynotes.composables.ImagePreviewComposable
import com.example.mynotes.composables.MainScreenComposable
import com.example.mynotes.composenavigation.Screen
import com.example.mynotes.enums.BottomSheetItems
import com.example.mynotes.enums.Theme
import com.example.mynotes.helper.CameraApi
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.services.RoomInterceptorService
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.utils.Preferences
import com.example.mynotes.utils.deepCopy
import com.example.mynotes.utils.getNoteIntentString
import com.example.mynotes.utils.getParcelable
import com.example.mynotes.utils.getUriEncoded
import com.example.mynotes.viewModel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()
    private var launcher: ActivityResultLauncher<Uri>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var currentUri: Uri? = null

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
        setObservers()
        startService(
            Intent(applicationContext, RoomInterceptorService::class.java)
        )
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
            MyNotesTheme(darkTheme = darkMode) {
                NavHostComposable(darkMode = darkMode, drawerState = drawerState) {
                    darkMode = it
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.toastLiveData().observe(this@MainActivity) {
            if (it.isNullOrEmpty()) return@observe
            showToast(it)
        }
    }

    @Composable
    private fun NavHostComposable(
        darkMode: Boolean,
        drawerState: DrawerState,
        onThemeChange: (Boolean) -> Unit,
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.MainScreen.route) {

            composable(Screen.MainScreen.route) {
                MainScreen(
                    darkMode = darkMode,
                    drawerState = drawerState,
                    onThemeChange = onThemeChange,
                    navigateToCreateNote = {
                        navController.navigate(
                            Screen.CreateNote.createRoute()
                        )
                    },
                    navigateToEditNote = { note ->
                        val noteString = getUriEncoded(note ?: NoteModel())
                        viewModel.setAll(note?.photoList ?: emptyList())
                        viewModel.setPhotos(viewModel.photoList)
                        navController.navigate(
                            Screen.EditNote.createRoute(noteString)
                        )
                    }
                )
            }

            composable(
                Screen.PreviewScreen.route,
                arguments = Screen.PreviewScreen.navArguments
            ) { arguments ->
                PreviewScreen(arguments, navController)
            }

            composable(
                Screen.EditNote.route,
                arguments = Screen.EditNote.navArguments
            ) {
                EditNoteScreen(
                    navController = navController,
                    arguments = it
                )
            }

            composable(
                Screen.CreateNote.route
            ) {
                CreateNoteScreen(navController)
            }
        }
    }

    @Composable
    private fun CreateNoteScreen(navController: NavHostController) {
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
            onImagePreview = { _, index ->
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

    @Composable
    private fun EditNoteScreen(navController: NavHostController, arguments: NavBackStackEntry) {
        val note = getParcelable(
            arguments.arguments, NOTE, NoteModel::class.java
        )
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
            onPhotoPreview = { _, index ->
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

    @Composable
    private fun PreviewScreen(arguments: NavBackStackEntry, navController: NavHostController) {
        val fromCreate = arguments.arguments?.getBoolean(Constants.FROM_CREATE)
        val initPos = arguments.arguments?.getInt(Constants.POS)
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

    @Composable
    private fun MainScreen(
        drawerState: DrawerState,
        navigateToCreateNote: () -> Unit,
        navigateToEditNote: (NoteModel?) -> Unit,
        darkMode: Boolean,
        onThemeChange: (Boolean) -> Unit
    ) {
        LaunchedEffect(Unit) {
            viewModel.refresh()
        }

        val notesList by viewModel.listFlow.collectAsState()

        var note by remember {
            mutableStateOf<NoteModel?>(null)
        }

        Surface(modifier = Modifier.fillMaxSize()) {
            MainScreenComposable(
                notesList = notesList,
                navigateToCreateNote = navigateToCreateNote,
                navigateToEditNote = navigateToEditNote,
                onClick = { navigateToSearchScreen() },
                onRefresh = { viewModel.refresh() },
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
                    updateTheme(update = { theme ->
                        onThemeChange(!theme)
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

    private fun navToTrashActivity() {
        Intent(this, TrashActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun createCopy(noteModel: NoteModel) {
        deepCopy(noteModel).apply {
            id = 0
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
        launcher = registerForActivityResult(PhotoActivityContract(onComplete = { uri, boolean ->
            if (boolean) {
                savePhoto(uri)
            }
        })) {}
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
        cameraApi.getOutputMediaFile(CameraApi.MEDIA_TYPE_IMAGE)?.let {
            currentUri = cameraApi.contentUriFromFile(file = it)
            launcher?.launch(currentUri)
        }
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

}

