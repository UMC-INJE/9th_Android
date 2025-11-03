package com.umc.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ItemTrackBinding
class TrackRVAdapter : RecyclerView.Adapter<TrackRVAdapter.ViewHolder>() {

    private val tracks = ArrayList<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracks[position], position)
    }

    override fun getItemCount(): Int = tracks.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Song>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Song, position: Int) {
            binding.itemOrder.text = String.format("%02d", position + 1)
            binding.itemTitle.text = track.title
            binding.itemSinger.text = track.singer
        }
    }
}
