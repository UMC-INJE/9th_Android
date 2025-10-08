package com.umc.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ItemAlbumBinding

class AlbumRVAdapter(
    private val albumList: List<Album>
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener { fun onItemClick(item: Album) }
    private var myItemClickListener: MyItemClickListener? = null
    fun setMyItemClickListener(l: MyItemClickListener) { myItemClickListener = l }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position], myItemClickListener)
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(
        private val binding: ItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Album, listener: MyItemClickListener?) {
            binding.itemAlbumTitleTv.text = item.title
            binding.itemAlbumSingerTv.text = item.singer
            binding.itemAlbumCoverImgIv.setImageResource(item.coverImg)
            binding.root.setOnClickListener { listener?.onItemClick(item) }
        }
    }
}
