package com.umc.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class BannerPagerAdapter(parent : Fragment, val pages : List<Fragment>) : FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = pages.size
    override fun createFragment(position: Int): Fragment = pages[position]
}