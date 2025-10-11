package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.umc.myapplication.R
import com.umc.myapplication.adapter.BannerPagerAdapter
import com.umc.myapplication.adapter.HomeNewProductAdapter
import com.umc.myapplication.databinding.FragmentHomeBinding
import com.umc.myapplication.model.homeBanner
import com.umc.myapplication.testData.testProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val intervalMs = 3000L

    @Volatile private var isUserDragging = false
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
    private val newProductList = testProductRepository.products.subList(2,4)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = BannerPagerAdapter(this, bannerFragmentList)

        binding.recyclerView.adapter = HomeNewProductAdapter(
            newProductList,
            onItemclick = {
                val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
                    isWishList = it.isWishList,
                    productId = it.productId
                )
                findNavController().navigate(action)
            }
        )
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) // ← 이 줄[web:22][web:21]


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.registerOnPageChangeCallback(pageCallback)

        // 화면이 STARTED 이상일 때 자동 스크롤 루프 시작, STOPPED 아래로 내려가면 자동 취소
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    autoScrollLoop()
                }
            }
        }

    }
    private suspend fun autoScrollLoop() {
        while (true) {
            delay(intervalMs)
            if (isUserDragging) continue
            val adapter = binding.viewPager.adapter ?: continue
            val count = adapter.itemCount
            if (count <= 1) continue
            val next = (binding.viewPager.currentItem + 1) % count
            binding.viewPager.setCurrentItem(next, true)
        }
    }
    private val pageCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager2.SCROLL_STATE_DRAGGING ->
                    isUserDragging = true
                ViewPager2.SCROLL_STATE_IDLE ->
                    isUserDragging = false
            }
        }
    }
    override fun onDestroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(pageCallback)
        _binding = null
        super.onDestroyView()
    }


}