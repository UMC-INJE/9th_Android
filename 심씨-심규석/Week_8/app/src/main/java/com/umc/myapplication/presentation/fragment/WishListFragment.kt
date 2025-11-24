package com.umc.myapplication.presentation.fragment

import android.os.Bundle
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
import com.umc.myapplication.databinding.FragmentWishListBinding
import com.umc.myapplication.presentation.adapter.WishListProductAdapter
import com.umc.myapplication.presentation.feature.UiProductState
import com.umc.myapplication.presentation.feature.UiProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.hypot

@AndroidEntryPoint
class WishListFragment : Fragment() {
    private var _binding: FragmentWishListBinding? = null
    private val binding get() = _binding!!

    private val uiViewModel: UiProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishListBinding.inflate(inflater, container, false)

        val adapter = WishListProductAdapter{
            val action = WishListFragmentDirections
                .actionWishListFragmentToProductDetailFragment(
                    productId = it.id
                )
            findNavController().navigate(action)
        }
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = GridLayoutManager(context, 2)

        viewLifecycleOwner.lifecycleScope.launch {
            uiViewModel.loadOnce()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiViewModel.state.collect { state ->
                    if (state is UiProductState.Data) {
                        // 즐겨찾기만 필터링하거나 그 상태를 적절히 반영해 adapter에 넘길 수 있음
                        val wishList = state.products.filter { it.liked }
                        adapter.submitList(wishList)
                    } else {
                        adapter.submitList(emptyList())
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
