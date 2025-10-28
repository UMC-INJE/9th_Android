package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umc.myapplication.databinding.FragmentBannerDetailBinding

class BannerDetailFragment : Fragment() {
    var _binding: FragmentBannerDetailBinding? = null
    private val binding get() = _binding!!
    private val args: BannerDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBannerDetailBinding.inflate(inflater, container, false)
        val resId = args.resId
        val title = args.title
        val description = args.description

        // UI 반영 예
        binding.banner.setImageResource(resId)
        binding.title.text = title
        binding.description.text = description

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }
}