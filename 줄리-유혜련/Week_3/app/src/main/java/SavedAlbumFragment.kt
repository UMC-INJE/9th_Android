package com.umc.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.myapplication.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedalbumBinding

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        binding= FragmentLockerSavedalbumBinding.inflate(inflater, container, false)
        return binding.root
    }
}