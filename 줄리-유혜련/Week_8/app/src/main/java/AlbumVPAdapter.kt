package com.umc.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(
    fragment: Fragment,
    private val albumTitle: String,
    private val artistName: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment()
            1 -> DetailFragment.newInstance(albumTitle, artistName)
            else -> VideoFragment()
        }
    }
}