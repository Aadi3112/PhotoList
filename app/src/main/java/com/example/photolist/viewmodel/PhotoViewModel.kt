package com.example.photolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.photolist.model.Photo
import com.example.photolist.repository.PhotoRepository

class PhotoViewModel constructor(private val photoRepository: PhotoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()

    fun getPhotoList(): LiveData<PagingData<Photo>> {
        return photoRepository.getAllPhotos().cachedIn(viewModelScope)
    }

}