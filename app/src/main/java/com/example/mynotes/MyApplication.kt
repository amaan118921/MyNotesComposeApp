package com.example.mynotes

import android.app.Application
import android.content.Intent
import com.example.mynotes.services.RoomInterceptorService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}