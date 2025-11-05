package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.myapplication.presentation.adapter.WishListProductAdapter
import com.umc.myapplication.databinding.FragmentWishListBinding
import com.umc.myapplication.data.mock.testProductRepository

class WishListFragment : Fragment() {
    private var _binding: FragmentWishListBinding? = null
    private val binding get() = _binding!!

    val list = testProductRepository.products.filter { it.isLiked }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishListBinding.inflate(inflater, container, false)

        binding.recycler.adapter = WishListProductAdapter(list) {
            val action = WishListFragmentDirections
                .actionWishListFragmentToProductDetailFragment(
                    productId = it.id
                )
            findNavController().navigate(action)
        }
        binding.recycler.layoutManager = GridLayoutManager(context, 2)
        
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}