package com.example.mynotes.eventbus

open class Event(private val event: EventBusEnum) {

    fun getEvent(): EventBusEnum {
        return event
    }
}