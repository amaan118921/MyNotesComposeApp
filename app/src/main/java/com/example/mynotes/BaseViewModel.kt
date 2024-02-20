package com.example.mynotes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mynotes.model.Photo

open class BaseViewModel: ViewModel() {

    var photoList: MutableList<Photo> = mutableListOf()
    private val _photoStateList = mutableStateListOf<Photo>()

    fun getPhotoListState(): List<Photo> = _photoStateList
    fun addPhoto(photo: Photo) {
        photoList.add(photo)
    }
    fun setAll(list: List<Photo>) {
        photoList.clear()
        photoList.addAll(list)
    }

    fun setPhotos(list: List<Photo>) {
        _photoStateList.clear()
        _photoStateList.addAll(list)
    }

    fun clearPhotos() {
        _photoStateList.clear()
        photoList.clear()
    }
}