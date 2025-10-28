package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.myapplication.R
import com.umc.myapplication.adapter.SearchProductAdapter
import com.umc.myapplication.adapter.WishListProductAdapter
import com.umc.myapplication.databinding.FragmentHomeBinding
import com.umc.myapplication.databinding.FragmentWishListBinding
import com.umc.myapplication.testData.testProductRepository

class WishListFragment : Fragment() {
    private var _binding: FragmentWishListBinding? = null
    private val binding get() = _binding!!

    val list = testProductRepository.products.filter { it.isWishList }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishListBinding.inflate(inflater, container, false)

        binding.recycler.adapter = WishListProductAdapter(list) {
            val action = WishListFragmentDirections
                .actionWishListFragmentToProductDetailFragment(
                    isWishList = it.isWishList,
                    productId = it.productId
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