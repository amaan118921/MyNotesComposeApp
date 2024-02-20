package com.example.mynotes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynotes.model.NoteModel
import com.example.mynotes.model.Photo
import com.example.mynotes.utils.isNull

open class BaseViewModel : ViewModel() {

    var photoList: MutableList<Photo> = mutableListOf()
    private val _photoStateList = mutableStateListOf<Photo>()

    private var _note: MutableLiveData<NoteModel> = MutableLiveData()
    val note: LiveData<NoteModel> = _note
    fun getPhotoListState(): List<Photo> = _photoStateList
    fun addPhoto(photo: Photo) {
        photoList.add(photo)
    }

    private fun setAll(list: List<Photo>) {
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

    fun setNote(noteModel: NoteModel) {
        _note.value = noteModel
        setAll(noteModel.photoList ?: emptyList())
        setPhotos(photoList)
    }

    fun removePhoto(pos: Int) {
        photoList.removeAt(pos)
    }

    fun updateNoteWithPhotos(pos: Int? = null, photo: Photo? = null) {
        if (pos.isNull() && photo.isNull()) return
        note.value?.apply {
            photoList = if (!photo.isNull()) addPhotoToList(
                photoList = photoList,
                photo!!
            ) else removePhotoFromList(photoList, pos!!)
            editedNow = true
            _note.value = this
        }
    }

    private fun addPhotoToList(photoList: List<Photo>?, photo: Photo): List<Photo> {
        mutableListOf<Photo>().apply {
            addAll(photoList ?: emptyList())
            add(photo)
            return this
        }
    }

    private fun removePhotoFromList(photoList: List<Photo>?, it: Int): List<Photo> {
        mutableListOf<Photo>().apply {
            addAll(photoList ?: emptyList())
            removeAt(it)
            return this
        }
    }

}
