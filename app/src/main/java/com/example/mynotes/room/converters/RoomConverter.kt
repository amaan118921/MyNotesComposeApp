package com.example.mynotes.room.converters


interface RoomConverter<T> {
    fun fromData(data: T?): String
    fun toData(value: String?): T?
}