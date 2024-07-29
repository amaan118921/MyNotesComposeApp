package com.example.mynotes

import org.greenrobot.eventbus.EventBus

class Constants {
    companion object {
        val event: EventBus = EventBus.getDefault()
        const val NOTE = "note"
        const val POS = "pos"
        const val FROM_CREATE: String = "fromCreate"
        const val APP_NAME: String = "mynotes"
        const val DARK = "DARK_THEME"
        const val TAG = "notes_exception"
        const val CHANNEL_ID = "room_interceptor"
        const val GRP_NOTIFICATION = "grp_notification"
    }
}