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
import com.google.gson.Gson
import com.umc.myapplication.Album
import com.umc.myapplication.AlbumRVAdapter
import com.umc.myapplication.BannerFragment
import com.umc.myapplication.BannerVPAdapter
import com.umc.myapplication.HeroSlideFragment
import com.umc.myapplication.HeroSlideVPAdapter
import com.umc.myapplication.MainActivity
import com.umc.myapplication.R
import com.umc.myapplication.Song
import com.umc.myapplication.data.db.SongDatabase
import com.umc.myapplication.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 1) DB 에서 앨범 목록 가져오기
        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.

        // 2) 어댑터 + 리사이클러뷰 세팅
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

        // 3) 카드 클릭 / Play 버튼 클릭 / 삭제 콜백
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{

            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onPlayClick(album: Album) {
                val song = Song(
                    title = album.title ?: "",
                    singer = album.singer ?: "",
                    coverImg = album.coverImg
                )

                (requireActivity() as MainActivity).updateMiniPlayer(song)
            }
        })

        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

}