package com.umc.myapplication.presentation.fragment

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.navArgs
import com.umc.myapplication.data.CartRepository
import com.umc.myapplication.databinding.FragmentProductDetailBinding
import com.umc.myapplication.domain.model.UiProduct
import com.umc.myapplication.presentation.feature.UiProductState
import com.umc.myapplication.presentation.feature.UiProductViewModel
import com.umc.myapplication.presentation.utils.setWishIcon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private val uiViewModel : UiProductViewModel by viewModels()
    @Inject lateinit var cartRepository: CartRepository
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ProductDetailFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiViewModel.loadOnce()
                uiViewModel.state.collect { s ->
                    when (s) {
                        is UiProductState.Data -> {
                            val products = s.products
                            val item = products.firstOrNull { it.id == args.productId }
                                ?: products.firstOrNull()
                            if (item != null) render(item)

                        }
                        is UiProductState.Error -> {
                            // 에러 처리
                        }
                        is UiProductState.Loading -> {
                            // 로딩 처리
                        }
                        is UiProductState.Empty -> {
                            //빈값 처리
                        }

                        UiProductState.Idle -> {
                            //이건 뭐 해야함?
                        }
                    }

                }
            }
        }

    }

    private fun render(item: UiProduct) {
        binding.headerTitle.text = item.name
        binding.resId.setImageResource(item.imageResource)
        binding.category.text = item.category
        binding.name.text = item.name
        binding.price.text = "US$${item.price}"
        binding.description.text = item.description
        binding.options.text = item.options.joinToString("\n") { "●$it" }
        binding.wishListIcon.setWishIcon(isWish = item.liked)
        binding.wishListButton.setOnClickListener {
            val toggled = !item.liked
            binding.wishListIcon.setWishIcon(isWish = toggled)
            uiViewModel.upsertIsLiked(item.id, toggled)
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addCartButton.setOnClickListener {
            cartRepository.saveProductId(item.id)
            val action = ProductDetailFragmentDirections
                .actionProductDetailFragmentToCartFragment(productId = item.id)

            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}