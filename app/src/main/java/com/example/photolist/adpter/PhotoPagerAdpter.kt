package com.example.photolist.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photolist.databinding.ItemPhotoListBinding
import com.example.photolist.model.Photo

class PhotoPagerAdpter : PagingDataAdapter<Photo, PhotoPagerAdpter.ViewHolder>(PhotoComparator) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position)!!
        holder.view.tvAuthor.text = photo.author
        holder.view.tvUrl.text = buildString {
        append("URL: ")
        append(photo.url)
    }
        Glide.with(holder.itemView.context).load(photo.download_url).into(holder.view.ivPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val view: ItemPhotoListBinding) : RecyclerView.ViewHolder(view.root) {
    }

    object PhotoComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
}