package com.example.photolist.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.photolist.data.remote.api.RetrofitService
import com.example.photolist.model.Photo
import com.example.photolist.util.Constant

class PhotoRepository constructor(private val retrofitService: RetrofitService) {
    fun getAllPhotos(): LiveData<PagingData<Photo>> {

        return Pager(
            config = PagingConfig(
                pageSize = Constant.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                PhotoPagingSource(retrofitService)
            }
            , initialKey = 1
        ).liveData
    }
}