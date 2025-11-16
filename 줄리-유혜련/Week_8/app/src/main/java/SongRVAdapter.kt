package com.umc.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ItemSongBinding

class SongRVAdapter(private val context: Context)
    : RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(private val binding: ItemSongBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.itemSongTitle.text = song.title
            binding.itemSongSinger.text = song.singer
            binding.itemSongImg.setImageResource(song.coverImg)
        }
    }

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}
