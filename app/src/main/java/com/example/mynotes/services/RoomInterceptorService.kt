package com.example.mynotes.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.mynotes.Constants.Companion.CHANNEL_ID
import com.example.mynotes.Constants.Companion.GRP_NOTIFICATION
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.example.mynotes.eventbus.DaoEvent
import com.example.mynotes.eventbus.Event
import com.example.mynotes.eventbus.EventBusEnum
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class RoomInterceptorService : Service() {

    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Subscribe
    fun onReceiveEvent(event: Event) {
        when (event.getEvent()) {
            EventBusEnum.POST_NOTIFICATION -> {
                postNotification(event as DaoEvent)
            }
        }
    }

    private fun postNotification(event: DaoEvent) {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
            }

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.room_chucker))
            .setGroup(GRP_NOTIFICATION)
            .setContentText(event.getDaoQueryType())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setGroupSummary(true)
            .build()
        notificationManager.notify((1..1000).random() , notification)
    }


    override fun onCreate() {
        super.onCreate()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        notificationManager = getSystemService(NotificationManager::class.java)
        setUpNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun setUpNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}