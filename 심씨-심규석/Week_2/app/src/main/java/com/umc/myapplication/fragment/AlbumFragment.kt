package com.umc.myapplication.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAlbumBinding
import com.umc.myapplication.databinding.FragmentHomeBinding

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val artist = arguments?.getString("artist")
        binding.title.text = title
        binding.artist.text = artist
        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }



    }
}