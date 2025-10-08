package com.umc.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ItemAlbumBinding

class AlbumRVAdapter(
    private val albumList: List<Album>
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(item: Album) // 카드 전체 클릭
        fun onPlayClick(item: Album) // Play 버튼 클릭
    }
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
            binding.itemAlbumTitle.text = item.title
            binding.itemAlbumSinger.text = item.singer
            binding.itemAlbumCoverImg.setImageResource(item.coverImg)

            // 전체 카드 클릭 → 앨범 화면 이동
            binding.root.setOnClickListener { listener?.onItemClick(item) }
            // play 버튼 클릭 → 미니플레이어 업데이트
            binding.itemAlbumPlayImg.setOnClickListener {
                listener?.onPlayClick(item)
            }
        }
    }
}
