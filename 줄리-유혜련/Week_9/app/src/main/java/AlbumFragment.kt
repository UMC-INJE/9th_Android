package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.umc.myapplication.Album
import com.umc.myapplication.AlbumVPAdapter
import com.umc.myapplication.Like
import com.umc.myapplication.MainActivity
import com.umc.myapplication.R
import com.umc.myapplication.data.db.SongDatabase
import com.umc.myapplication.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    val information = arrayListOf("수록곡", "상세정보", "영상")
    private lateinit var title: String
    private lateinit var artist: String
    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumData = arguments?.getString("album")
        val gson = Gson()

        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.id)

        setViews(album)
        initViewPager()
        setClickListeners(album)


        return binding.root
    }

    private fun setViews(album: Album) {
        title = album.title
        artist = album.singer

        binding.title.text = album.title
        binding.artist.text = album.singer
        binding.albumImage.setImageResource(album.coverImg!!)

        if(isLiked) {
            binding.Like.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.Like.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album) {
        val userId: Int = getJwt()

        binding.Like.setOnClickListener {
            if (isLiked) {
                binding.Like.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.id)
            } else {
                binding.Like.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }

            isLiked = !isLiked
        }

        binding.backButton.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun disLikeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)

        return jwt
    }

    private fun initViewPager() {
        //init viewpager
        val albumAdapter = AlbumVPAdapter(this, title, artist)
        binding.albumContentVp.adapter = albumAdapter

        // 4) TayLayout 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }
}
