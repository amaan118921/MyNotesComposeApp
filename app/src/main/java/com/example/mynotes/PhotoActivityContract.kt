package com.example.mynotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import com.example.mynotes.utils.getParcelable

class PhotoActivityContract(
    private val onComplete: ((Uri?, Boolean) -> Unit)? = null
) : ActivityResultContract<Uri, Boolean>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        val uri = getParcelable(
            intent?.extras, MediaStore.EXTRA_OUTPUT, Uri::class.java
        )
        onComplete?.invoke(uri, resultCode == Activity.RESULT_OK)
        return resultCode == Activity.RESULT_OK
    }
}