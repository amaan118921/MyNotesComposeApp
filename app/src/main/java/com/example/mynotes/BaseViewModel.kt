package com.example.mynotes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynotes.model.Photo
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {

    val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            _toastLiveData.value = throwable.message.toString()
        }
    }

    private var _toastLiveData: MutableLiveData<String> = MutableLiveData("")
    fun toastLiveData(): LiveData<String> = _toastLiveData

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

    fun removePhoto(pos: Int) {
        photoList.removeAt(pos)
    }


}
