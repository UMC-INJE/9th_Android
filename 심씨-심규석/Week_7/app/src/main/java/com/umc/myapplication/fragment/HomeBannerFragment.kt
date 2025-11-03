package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.databinding.FragmentHomeBannerBinding
import com.umc.myapplication.model.homeBanner

class HomeBannerFragment : Fragment() {
    companion object {
        fun newInstance(banner : homeBanner) = HomeBannerFragment().apply {
            arguments = Bundle().apply {
                putInt("resId", banner.resId)
                putString("title", banner.title)
                putString("description", banner.description)

            }
        }
    }
    private var _binding: FragmentHomeBannerBinding? = null
    private val binding get() = _binding!!

    private val resId by lazy { arguments?.getInt("resId") ?: 0 }
    private val title by lazy { arguments?.getString("title") ?: "" }
    private val description by lazy { arguments?.getString("description") ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBannerBinding.inflate(inflater, container, false)
        binding.banner.setImageResource(resId)
        binding.banner.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToBannerDetailFragment(resId, title, description)
            findNavController().navigate(action)
        }

        return binding.root
    }
}