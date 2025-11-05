package com.umc.myapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umc.myapplication.databinding.FragmentProductDetailBinding
import com.umc.myapplication.data.mock.testProductRepository
import com.umc.myapplication.presentation.utils.setWishIcon

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ProductDetailFragmentArgs by navArgs()
    private val productList = testProductRepository.products
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        val passedProductId = args.productId
        val item = productList.firstOrNull { it.id == passedProductId }
            ?: productList.first()

        binding.headerTitle.text = item.name
        binding.resId.setImageResource(item.imageResource)
        binding.category.text = item.category
        binding.name.text = item.name
        binding.price.text = "US$${item.price}"
        binding.description.text = item.description
        binding.options.text = item.options.joinToString("\n") { "●$it" }
        binding.wishListIcon.setWishIcon(isWish = item.isLiked)
        binding.wishListButton.setOnClickListener { view ->
            val isWishList = !item.isLiked
            item.isLiked = isWishList
            binding.wishListIcon.setWishIcon(isWish = item.isLiked)
        }
        binding.backButton.setOnClickListener {
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("isWishList_result_${item.id}", item.isLiked)
            findNavController().popBackStack()
        }
        binding.addCartButton.setOnClickListener {
            val action = ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment(
                productId = item.id
            )
            findNavController().navigate(action)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}