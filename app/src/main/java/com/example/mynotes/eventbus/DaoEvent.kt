package com.example.mynotes.eventbus

class DaoEvent(eventBusEnum: EventBusEnum, private val daoQueryType: String) :
    Event(eventBusEnum) {

    fun getDaoQueryType(): String {
        return daoQueryType
    }
}