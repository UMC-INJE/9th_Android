package com.umc.myapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("fragment", "onViewCreated: create")

        // 상단 타이틀/정보
        binding.homeTitle.text = "달밤의 감성 산책"
        binding.totalSong.text = "총 10곡 2025.03.30"


        // 첫 번째 아이템
        val first = binding.songFirItem  // include @+id/songFirItem에 대응하는 바인딩
        first.title.text = "Lady"
        first.artist.text = "Kenshi Yonezu"
        Glide.with(view)
            .load(R.drawable.img_album_kenshi1) // 로컬 드로어블로 교체
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_home_setting)
            .error(R.drawable.ic_home_setting)
            .into(first.corverImg)



        // 두 번째 아이템 플레이 리스트
        val second = binding.songSecItem // include @+id/songSecItem
        second.title.text = "Spinning Globe"
        second.artist.text = "Kenshi Yonezu"
        Glide.with(view)
            .load(R.drawable.img_album_kenshi2) // 로컬 드로어블로 교체
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_home_setting)
            .error(R.drawable.ic_home_setting)
            .into(second.corverImg)

        val todayFirst = binding.todaySong1
        val todaySecond = binding.todaySong2

        todayFirst.title.text = "Lady"
        todayFirst.artist.text = "Kenshi Yonezu"

        todaySecond.title.text = "Spinning Globe"
        todaySecond.artist.text = "Kenshi Yonezu"
        // 첫 번째 아이템 플레이 리스트 이미지 변경
        Glide.with(view)
            .load(R.drawable.img_album_kenshi1) // 로컬 드로어블로 교체
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_home_setting)
            .error(R.drawable.ic_home_setting)
            .into(todayFirst.corverImg)
        // 두 번째 아이템 플레이 리스트 이미지 변경
        Glide.with(view)
            .load(R.drawable.img_album_kenshi2) // 로컬 드로어블로 교체
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_home_setting)
            .error(R.drawable.ic_home_setting)
            .into(todaySecond.corverImg)

        todayFirst.root.setOnClickListener {
            val sendData = Bundle().also {
                it.putString("title", todayFirst.title.text.toString())
                it.putString("artist", todayFirst.artist.text.toString())
            }
            findNavController().navigate(R.id.albumFragment,sendData )
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
