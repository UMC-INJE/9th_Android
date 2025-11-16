package com.umc.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.myapplication.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // AlbumFragment -> Adapter -> DetailFragment.newInstance(...)로 전달된 값 읽기
        val albumTitle = arguments?.getString(ARG_TITLE).orEmpty()
        val artistName = arguments?.getString(ARG_ARTIST).orEmpty()

        // fragment_detail.xml 안의 TextView id에 맞춰 세팅
        binding.tvAlbumName.text = albumTitle
        binding.tvComposer.text = artistName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_ARTIST = "arg_artist"

        // 어댑터에서 호출할 팩토리 메서드
        fun newInstance(title: String, artist: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_ARTIST, artist)
            }
        }
    }
}
