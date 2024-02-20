package com.example.mynotes.room.converters

import androidx.room.TypeConverter
import com.example.mynotes.model.Photo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

class PhotoConverter : RoomConverter<List<Photo>> {

    @TypeConverter
    override fun fromData(data: List<Photo>?): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    override fun toData(value: String?): List<Photo>? {
        val listType: Type = object : TypeToken<List<Photo?>?>() {}.type
        return try {
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            null
        }
    }

}