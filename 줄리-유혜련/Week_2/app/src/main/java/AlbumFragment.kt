package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 값 적용
        val title = requireArguments().getString(KEY_TITLE, "")
        val artist = requireArguments().getString(KEY_ARTIST, "")
        val imageRes = requireArguments().getInt(KEY_IMAGE_RES, 0)

        binding.title.text = title
        binding.artist.text = artist
        if (imageRes != 0) binding.albumImage.setImageResource(imageRes)

        // 뒤로가기
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_ARTIST = "artist"
        const val KEY_IMAGE_RES = "imageRes"
    }
}
