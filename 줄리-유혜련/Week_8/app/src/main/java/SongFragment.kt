package com.umc.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.myapplication.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!
    private var isToggled = false

    // 어댑터 선언
    private lateinit var trackAdapter: TrackRVAdapter

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        _binding= FragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 1. 토글 버튼 동작
        binding.songMixoffTg.setOnClickListener {
            isToggled = !isToggled
            if (isToggled) {
                binding.songMixoffTg.setImageResource(R.drawable.btn_toggle_on)
            } else {
                binding.songMixoffTg.setImageResource(R.drawable.btn_toggle_off)
            }
        }
        // 2. RecyclerView 초기화
        initRecyclerView()
    }

    private fun initRecyclerView() {
        trackAdapter = TrackRVAdapter()
        binding.trackRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.trackRv.adapter = trackAdapter

        // 더미 데이터
        val tracks = listOf(
            Song(title = "라일락", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(title = "Flu", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(title = "Coin", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(title = "봄 안녕 봄", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(title = "Celebrity", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2),
            Song(title = "돌림노래 (Feat. DEAN)", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2)
        )

        // 어댑터에 데이터 전달
        trackAdapter.submitList(tracks)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}