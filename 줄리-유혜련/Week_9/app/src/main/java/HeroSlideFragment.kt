package com.umc.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.myapplication.databinding.FragmentHeroSlideBinding

class HeroSlideFragment : Fragment() {

    private var _binding: FragmentHeroSlideBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_META = "meta"
        private const val ARG_SONG_TITLE = "songTitle"
        private const val ARG_SONG_ARTIST = "songArtist"

        fun newInstance(
            title: String,
            meta: String,
            songTitle: String,
            songArtist: String
        ): HeroSlideFragment = HeroSlideFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_META, meta)
                putString(ARG_SONG_TITLE, songTitle)
                putString(ARG_SONG_ARTIST, songArtist)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroSlideBinding.inflate(inflater, container, false)
        val args = requireArguments()
        binding.bannerTitle.text = args.getString(ARG_TITLE)
        binding.bannerMeta.text = args.getString(ARG_META)
        binding.bannerSongTitle.text = args.getString(ARG_SONG_TITLE)
        binding.bannerSongArtist.text = args.getString(ARG_SONG_ARTIST)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
