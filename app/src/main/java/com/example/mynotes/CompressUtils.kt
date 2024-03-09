package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import com.example.mynotes.helper.CameraApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompressUtils @Inject constructor(private val cameraApi: CameraApi, @ApplicationContext private val context: Context) {
    fun getCompressedPath(file: File?, destWidth: Int): File? {
        try {
            val bitmap = BitmapFactory.decodeFile(file?.absolutePath)

            val origWidth = bitmap.width
            val origHeight = bitmap.height

            val destHeight = origHeight / (origWidth / destWidth)

            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)

            val file = cameraApi.getOutputMediaFile(CameraApi.MEDIA_TYPE_IMAGE)
            val fos =
                FileOutputStream(file)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.close()
            return file
        } catch (e: Exception) {
            return null
        }
    }

    @SuppressLint("Recycle")
    fun isPortrait(uri: Uri): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val inputStream = context.contentResolver.openInputStream(uri)


        val exifInterface = inputStream?.let { ExifInterface(it) }

        val origWidth = exifInterface?.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
        val origHeight = exifInterface?.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

        return true
    }
}