package com.example.photolist.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.photolist.data.remote.api.RetrofitService
import com.example.photolist.model.Photo
import com.example.photolist.util.Constant

class PhotoPagingSource(private val apiService: RetrofitService) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {

        return try {
            val position = params.key ?: 1
            val response = apiService.getPhotoList(position.toString(), Constant.PAGE_SIZE)
            LoadResult.Page(
                data = response.body()!!, prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}