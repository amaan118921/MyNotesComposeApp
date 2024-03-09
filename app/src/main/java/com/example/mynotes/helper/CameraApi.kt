package com.example.mynotes.helper

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.mynotes.R
import com.example.mynotes.utils.getDateFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraApi @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val MEDIA_TYPE_IMAGE = 1
    }

    /** Create a file Uri for saving an image or video */
    fun getOutputMediaFileUri(type: Int): Uri? {
        val file = getOutputMediaFile(type)
        file?.let {
            return contentUriFromFile(it)
        }
        return null
    }

    fun contentUriFromFile(file: File?): Uri? {
        return file?.let {
            FileProvider.getUriForFile(
                context, context.getString(R.string.authorities),
                it
            )
        }
    }

    /** Create a File for saving an image or video */
    fun getOutputMediaFile(type: Int): File? {
        // TODO:
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mediaStorageDir?.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }
        val pattern = "yyyyMMdd_HHmmss"
        val timeStamp = getDateFormat(pattern).format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir?.path}${File.separator}IMG_$timeStamp.jpg")
            }

            else -> null
        }
    }
}