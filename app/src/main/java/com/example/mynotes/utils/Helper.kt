package com.example.mynotes.utils

import android.net.Uri
import com.google.gson.Gson

fun <T> getUriEncoded(obj: T): String {
    val json = Gson().toJson(obj)
    return Uri.encode(json)
}

fun Any?.isNull(): Boolean {
    return this==null
}