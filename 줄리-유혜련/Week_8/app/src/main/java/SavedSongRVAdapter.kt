package com.umc.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ItemSongBinding

class SavedSongRVAdapter : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.itemSongMore.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.itemSongTitle.text = song.title
            binding.itemSongSinger.text = song.singer
            binding.itemSongImg.setImageResource(song.coverImg)
        }
    }
}
