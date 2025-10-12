package com.umc.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentProductDetailBinding
import com.umc.myapplication.model.ProductDetail

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ProductDetailFragmentArgs by navArgs()
    private val productList = listOf<ProductDetail>(
        ProductDetail(
            productId = 1,
            resId = R.drawable.img_search_item1,
            name ="Nike Everyday Plus Cushioned",
            isWishList = true,
            description = "The Nike Everyday Plus Cushioned Socks bring comfort to your workout with extra cushioning under the heel and forefoot and a snug, supportive arch band. Sweat-wicking power and breathability up top help keep your feet dry and cool to help push you through that extra set.",
            price = 10,
            category = "Training Crew Socks",
            options = listOf(
                "Shown: Multi-Color",
                "Style: SX6897-965"),
            ),
        ProductDetail(
            productId = 2,
            resId = R.drawable.img_search_item1,
            name ="Nike Everyday Plus Cdddushioned",
            isWishList = false,
            description = "Training Ankle Sockds (6 Pairs)",
            price = 300,
            category = "",
            options = listOf(),
            ),
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val passedIsWish = args.isWishList
        val passedProductId = args.productId
        val item = productList.firstOrNull { it.productId == passedProductId }
            ?: productList.first()
        item.isWishList = passedIsWish
        binding.headerTitle.text = item.name
        binding.resId.setImageResource(item.resId)
        binding.category.text = item.category
        binding.name.text = item.name
        binding.price.text = "US$${item.price}"
        binding.description.text = item.description
        binding.options.text = item.options.joinToString("\n") { "●$it" }
        binding.wishListIcon.setImageResource(
            if (item.isWishList) R.drawable.ic_red_heart
            else R.drawable.ic_bnv_wish_list)
        binding.wishListButton.setOnClickListener { view ->
            val isWishList = !item.isWishList
            item.isWishList = isWishList
            binding.wishListIcon.setImageResource(
                if (isWishList) R.drawable.ic_red_heart
                else R.drawable.ic_bnv_wish_list)
        }
        binding.backButton.setOnClickListener {
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("isWishList_result_${item.productId}", item.isWishList)
            findNavController().popBackStack()
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