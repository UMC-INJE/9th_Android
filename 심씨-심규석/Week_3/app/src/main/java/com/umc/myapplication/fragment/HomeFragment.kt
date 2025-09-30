package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.myapplication.R
import com.umc.myapplication.adapter.BannerPagerAdapter
import com.umc.myapplication.databinding.FragmentHomeBinding
import com.umc.myapplication.model.homeBanner

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bannerList = arrayListOf(
        homeBanner(resId = R.drawable.img_home_banner1,
            title = "Bench Squad Vibe \uD83D\uDE0E",
            description = "Hip-hop energy meets game-day calm as two teammates break down a simple courtside routine from the bench—first fast, then slow, then all together for a confident duo pose. Kids can pair up, match seats and footwork, and finish with a synchronized ball hand-off to create a mini bench-shot challenge of their own."),
        homeBanner(R.drawable.img_home_banner2,
            title = "Soyeon’s Dance \n" +
                    "Challenge \uD83D\uDE0E",
            description ="Hip hop dancer Soyeon Jang shows us an epic dance challenge in the latest Playlist episode. Soyeon dances three parts of the routine - first fast, then slow. Then she combines all the moves for an awesome dance party with her buddy, Disco Dancer. Kids will get inspired to dance along and make up a dance routine of their own."),


        homeBanner(R.drawable.img_home_banner3,
            title = "One-Hand Focus \uD83C\uDFC0",
            description ="A clean monochrome fit sets the stage for a pregame balance drill—start with stance, lock the wrist, then bring the gaze forward, first fast, then slow, then combine for a steady release pose. Kids can try the one-hand hold, refine posture, and craft a personal warm-up snapshot that shows calm control before tip-off."),
    )
    private val bannerFragmentList = bannerList.map {
        HomeBannerFragment.newInstance(it)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = BannerPagerAdapter(this, bannerFragmentList)

        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}