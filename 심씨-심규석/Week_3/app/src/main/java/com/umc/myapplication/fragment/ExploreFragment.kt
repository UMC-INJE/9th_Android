package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }
}