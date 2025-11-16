package com.umc.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockorVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}