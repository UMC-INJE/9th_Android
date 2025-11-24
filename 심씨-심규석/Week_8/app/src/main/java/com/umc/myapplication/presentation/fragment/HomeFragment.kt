package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.umc.myapplication.R
import com.umc.myapplication.presentation.adapter.BannerPagerAdapter
import com.umc.myapplication.presentation.adapter.HomeNewProductAdapter
import com.umc.myapplication.databinding.FragmentHomeBinding
import com.umc.myapplication.domain.model.homeBanner
import com.umc.myapplication.data.mock.testProductRepository
import com.umc.myapplication.domain.model.UiProduct
import com.umc.myapplication.presentation.feature.UiProductState
import com.umc.myapplication.presentation.feature.UiProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val uiViewModel : UiProductViewModel by activityViewModels()
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
    private val newProductIdSet = listOf(1,3,4).toSet()
    private val newProductList = testProductRepository.products.subList(2,4)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = BannerPagerAdapter(this, bannerFragmentList)

        val newProductAdapter = HomeNewProductAdapter(onItemclick = { product ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToProductDetailFragment(productId = product.id)
            findNavController().navigate(action)
        })
        binding.recyclerView.adapter = newProductAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) // ← 이 줄[web:22][web:21]


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiViewModel.loadOnce()
                //테스트 영역
//                testProductRepository.userLikedProductIds.forEach {
//                    uiViewModel.upsertLiked(it)
//                }


                uiViewModel.state.collect { s ->
                    Log.d("state", s.toString())
                    when (s) {
                        is UiProductState.Data -> {
                            val products = s.products
                            val items = products.filter { it.id in newProductIdSet }
                            renderProducts(items)
                            Log.d("products", "onViewCreated: " + products)
                            Log.d("items", "onViewCreated: " + items)
                        }
                        is UiProductState.Error -> {
                            // 에러 처리
                        }
                        is UiProductState.Loading -> {
                            // 로딩 처리
                        }
                        is UiProductState.Empty -> {
                            //빈값 처리
                        }

                        UiProductState.Idle -> {
                            //이건 뭐 해야함?
                        }
                    }

                }
            }
        }

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
    private fun renderProducts(items: List<UiProduct>) {

        (binding.recyclerView.adapter as? HomeNewProductAdapter)?.submitList(items)
        // 가시성/스켈레톤 제어 예시
        binding.recyclerView.visibility = View.VISIBLE
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