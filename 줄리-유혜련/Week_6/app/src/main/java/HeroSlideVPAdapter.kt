package com.umc.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HeroSlideVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val items = mutableListOf<Fragment>()
    fun addFragment(f: Fragment) {
        items.add(f)
        notifyItemInserted(items.lastIndex)
    }
    override fun getItemCount() = items.size
    override fun createFragment(position: Int): Fragment = items[position]
}
