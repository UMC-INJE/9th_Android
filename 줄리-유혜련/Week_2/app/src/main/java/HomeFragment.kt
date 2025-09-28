package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val todayImageRes = R.drawable.home_song_img2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val card = binding.todaySong1

        card.corverImg.setImageResource(todayImageRes)

        // 공용 이동 함수: 이미지/카드 어디를 눌러도 동일 동작
        val goAlbum: (View) -> Unit = {
            val args = bundleOf(
                AlbumFragment.KEY_TITLE to card.title.text.toString(),
                AlbumFragment.KEY_ARTIST to card.artist.text.toString(),
                AlbumFragment.KEY_IMAGE_RES to todayImageRes
            )
            val dest = AlbumFragment().apply { arguments = args }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, dest)
                .addToBackStack("Album")
                .commitAllowingStateLoss()
        }

        card.root.setOnClickListener(goAlbum)
        card.corverImg.setOnClickListener(goAlbum)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}