package com.example.photolist.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.photolist.adpter.PhotoPagerAdpter
import com.example.photolist.data.remote.api.RetrofitService
import com.example.photolist.databinding.ActivityMainBinding
import com.example.photolist.databinding.LayoutDialogBinding
import com.example.photolist.model.Photo
import com.example.photolist.model.PhotoClickListner
import com.example.photolist.repository.PhotoRepository
import com.example.photolist.viewmodel.PhotoViewModel
import com.example.photolist.viewmodel.PhotoViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: PhotoViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        val repository = PhotoRepository(retrofitService)

        val adapter = PhotoPagerAdpter(PhotoClickListner {
            openDialog(it)
            //Toast.makeText(this, it.author, Toast.LENGTH_SHORT).show()
        })
        binding.rvPhotoList.adapter = adapter

        binding.swipeToRefresh.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = false
            adapter.refresh()
        }

        viewModel = ViewModelProvider(
            this,
            PhotoViewModelFactory(repository)
        ).get(PhotoViewModel::class.java)

        viewModel.errorMessage.observe(this) {

            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            )
                binding.progressDialog.isVisible = true
            else {
                binding.progressDialog.isVisible = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }


        lifecycleScope.launch {
            viewModel.getPhotoList().observe(this@MainActivity) {
                it?.let {
                    adapter.submitData(lifecycle, it)

                }
            }
        }
    }

    private fun openDialog(photo: Photo) {

        val dialog = BottomSheetDialog(this)
        //dialog.setContentView(R.layout.layout_dialog)
        val dialogBinding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.tvAuthor.text = photo.author
        dialogBinding.tvUrl.text = photo.url
        dialogBinding.tvDownloadUrl.text = photo.download_url
        dialog.show()
    }
}