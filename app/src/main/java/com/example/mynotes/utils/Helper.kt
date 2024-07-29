package com.example.mynotes.utils

import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.example.mynotes.Constants
import com.example.mynotes.model.NoteModel
import com.google.gson.Gson

fun <T> getUriEncoded(obj: T): String {
    val json = Gson().toJson(obj)
    return Uri.encode(json)
}

inline fun <reified T> deepCopy(obj: T): T {
    val json = Gson().toJson(obj)
    return Gson().fromJson(json, T::class.java)
}

fun <T> getParcelable(bundle: Bundle?, key: String, className: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getParcelable(key, className)
    } else bundle?.getParcelable(Constants.NOTE)
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun getNoteIntentString(noteModel: NoteModel?): String {
    noteModel?.let {
        return "Title - ${it.title}\nBody - ${it.body}"
    }
    return ""
}
