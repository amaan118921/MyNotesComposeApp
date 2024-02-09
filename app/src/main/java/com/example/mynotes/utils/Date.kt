package com.example.mynotes.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

fun getTime(): String {
    val pattern = "hh:mm a"
    return getDateFormat(pattern = pattern).format(Calendar.getInstance().timeInMillis)
}

fun getDate(): String {
    val pattern = "dd MMMM yyyy"
    return getDateFormat(pattern = pattern).format(Calendar.getInstance().timeInMillis)
}

@SuppressLint("SimpleDateFormat")
fun getDateFormat(pattern: String): SimpleDateFormat {
    return SimpleDateFormat(pattern)
}