package com.example.mynotes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.mynotes.helper.CameraApi
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompressUtils @Inject constructor(private val cameraApi: CameraApi) {
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

    fun isPortrait(file: File?): Boolean {
        val bitmap = BitmapFactory.decodeFile(file?.path)

        val origWidth = bitmap.width
        val origHeight = bitmap.height

        return origHeight > origWidth
    }
}