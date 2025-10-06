package com.umc.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.myapplication.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!
    private var isToggled = false

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

        binding.songMixoffTg.setOnClickListener {
            isToggled = !isToggled
            if (isToggled) {
                binding.songMixoffTg.setImageResource(R.drawable.btn_toggle_on)
            } else {
                binding.songMixoffTg.setImageResource(R.drawable.btn_toggle_off)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}