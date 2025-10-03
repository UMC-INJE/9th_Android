package com.umc.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    //fragmentlist에 담길 데이터의 갯수
    override fun getItemCount(): Int = fragmentlist.size

    //fragmentlist 안에 있는 item들(=fragment들)을 생성해주는 함수
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment) //fragmentlist에 인자값으로 받은 fragment를 추가할 거라는 코드
        notifyItemInserted(fragmentlist.size-1)
    }

}