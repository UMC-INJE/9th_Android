package com.umc.myapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.myapplication.presentation.adapter.SearchUiProductAdapter
import com.umc.myapplication.databinding.FragmentSearchBinding
import com.umc.myapplication.presentation.feature.UiProductState
import com.umc.myapplication.presentation.feature.UiProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val uiViewModel : UiProductViewModel by viewModels ()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val adapter = SearchUiProductAdapter(
            onItemClick = {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToProductDetailFragment(
                        productId = it.id
                    )
                findNavController().navigate(action)
            },
            onToggleWish = {
                Log.d("searchFragment", "onCreateView: ${it.liked}")
                uiViewModel.upsertIsLiked(it.id, !it.liked)
            }
        )
        binding.recycler.adapter = adapter

        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        // Inflate the layout for this fragment
        viewLifecycleOwner.lifecycleScope.launch {
            uiViewModel.loadOnce()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiViewModel.state.collect { s ->
                    Log.d("searchFragment", "onCreateView: $s")
                    when (s) {
                        is UiProductState.Data -> adapter.submitList(s.products.toList())
                        is UiProductState.Empty -> adapter.submitList(emptyList())
                        else -> Unit
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}