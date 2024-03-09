package com.example.mynotes.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mynotes.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(@ApplicationContext private val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        initSharedPreferences()
    }

    private fun initSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setSharedPreferences(key: String, value: String) {
        editor.putString(key, value).apply()
    }
    fun setSharedPreferencesInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun getSharedPreferencesString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun getSharedPreferencesInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun clearSharedPreferences() {
        editor.clear().apply()
    }

    fun deleteSharedPreferences(key: String) {
        editor.remove(key).apply()
    }
}
