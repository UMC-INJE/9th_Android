package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.myapplication.Album
import com.umc.myapplication.AlbumRVAdapter
import com.umc.myapplication.BannerFragment
import com.umc.myapplication.BannerVPAdapter
import com.umc.myapplication.HeroSlideFragment
import com.umc.myapplication.HeroSlideVPAdapter
import com.umc.myapplication.MainActivity
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        albumDatas.apply {
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(item: Album) {
                val args = bundleOf(
                    AlbumFragment.KEY_TITLE to item.title,
                    AlbumFragment.KEY_ARTIST to item.singer,
                    AlbumFragment.KEY_IMAGE_RES to item.coverImg
                )
                val dest = AlbumFragment().apply { arguments = args }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, dest)     // main_frm 이 FrameLayout 컨테이너인지 확인
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }

            override fun onPlayClick(item: Album) {
                val first = item.songs.firstOrNull()
                val titleForMini = first?.title ?: item.title
                val artistForMini = first?.singer ?: item.singer

                (requireActivity() as MainActivity).updateMiniPlayer(
                    title = titleForMini,
                    artist = artistForMini,
                )
            }
        })

        val heroAdapter = HeroSlideVPAdapter(this).apply {
            addFragment(
                HeroSlideFragment.newInstance(
                    title = "오늘의 추천 노래",
                    meta = "총 17곡 2025.03.31",
                    songTitle = "Pop Song",
                    songArtist = "Kenshi Yonezu"
                )
            )
            addFragment(
                HeroSlideFragment.newInstance(
                    title = "달밤의 감성 산책",
                    meta = "총 10곡 2025.03.30",
                    songTitle = "Lady",
                    songArtist = "Kenshi Yonezu"
                )
            )
        }
        binding.homeHeroVp.apply {
            adapter = heroAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 1
        }

        // 인디케이터 연결 (CircleIndicator3)
        binding.homeBannerIndicator.setViewPager(binding.homeHeroVp)

        // 코루틴 자동 슬라이드 (3초 간격)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val vp = binding.homeHeroVp
                while (isActive) {
                    delay(3000)
                    val count = vp.adapter?.itemCount ?: 0
                    if (count > 1) {
                        vp.setCurrentItem((vp.currentItem + 1) % count, true)
                    }
                }
            }
        }

        val bannerAdapter = BannerVPAdapter(this).apply {
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        }
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }
}