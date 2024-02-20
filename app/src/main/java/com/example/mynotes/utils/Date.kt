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
    val date = getDateFormat(pattern = pattern).format(Calendar.getInstance().timeInMillis)
    return parseToDateFormat(date)
}

fun parseToDateFormat(date: String): String {
    val arr = date.split(" ")
    if (arr.size < 3) return date
    return arr[1] + " " + arr[0].append(",") + " " + arr[2]
}
fun String.append(string: String): String {
    return this + string
}

@SuppressLint("SimpleDateFormat")
fun getDateFormat(pattern: String): SimpleDateFormat {
    return SimpleDateFormat(pattern)
}