package com.umc.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.myapplication.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SavedSongRVAdapter()

        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter

        val songs = arrayListOf(
            Song(id = 1, title = "Lilac", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(id = 2, title = "Blueming", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(id = 3, title = "Love Poem", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(id = 4, title = "Coin", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(id = 5, title = "Celebrity", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2)
        )
        songRVAdapter.addSongs(songs)
    }
}