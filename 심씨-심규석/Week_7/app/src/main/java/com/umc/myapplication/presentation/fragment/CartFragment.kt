package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.umc.myapplication.data.CartRepository
import com.umc.myapplication.databinding.FragmentCartBinding
import com.umc.myapplication.presentation.utils.setUnderlinedSpannable
import com.umc.myapplication.domain.model.UiProduct
import com.umc.myapplication.presentation.feature.UiProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val uiViewModel : UiProductViewModel by activityViewModels()
    @Inject lateinit var cartRepository: CartRepository
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = CartContext(
            binding = binding,
            navigator = { directions ->
                view.findNavController().navigate(directions)
            },
            coroutineScope = viewLifecycleOwner.lifecycleScope
        )
        val productId = cartRepository.getProductId()
        val ui = uiViewModel.getUiProductById(productId)
        ctx.setProduct(ui)

        binding.orderButton.setOnClickListener { ctx.onOrderClick() }
    }

}
interface CartState {
    fun render(binding: FragmentCartBinding)
    fun onOrderClick(ctx: CartContext)
}
class EmptyState : CartState {
    override fun render(binding: FragmentCartBinding) {
        binding.cartEmpty.root.visibility = View.VISIBLE
        binding.cartFill.root.visibility = View.GONE
    }
    override fun onOrderClick(ctx: CartContext) {
        // 비었으면 검색 화면으로 이동
        ctx.navigateToSearch()
    }
}

class FilledState(private val product: UiProduct) : CartState {

    override fun render(binding: FragmentCartBinding) {
        binding.cartEmpty.root.visibility = View.GONE
        binding.cartFill.root.visibility = View.VISIBLE

        binding.cartFill.name.text = product.name
        binding.cartFill.description.text = product.shortDescription
        binding.cartFill.imageView.setImageResource(product.imageResource)
        binding.cartFill.count.text = "1"

        setUnderlinedSpannable(
            textView = binding.cartFill.delebery,
            fullText = "Arrives Wed, 11May\nto Fri, 13 May Edit Location",
            underlineTargets = listOf("Edit Location"),
        )
        binding.cartFill.productPrice.text = "US$${product.price}"
        binding.cartFill.price.text = "US$${product.price} + Tax"
        binding.cartFill.deliveryFee.text = "Standard - Free"

        binding.cartFill.promotion.setOnClickListener {  }


    }
    override fun onOrderClick(ctx: CartContext) {
        ctx.coroutineScope.launch {
            ctx.showLoading()
            delay(2000)
            ctx.clearCart()
            ctx.hideLoading()
        }

    }
}

class CartContext(
    private val binding: FragmentCartBinding,
    private val navigator: (NavDirections) -> Unit,
    val coroutineScope: LifecycleCoroutineScope,
) {
    private var state: CartState = EmptyState()
    private var currentProductId : Int? = null
    fun setState(newState: CartState) {
        state = newState
        state.render(binding)
    }
    fun onOrderClick() = state.onOrderClick(this)

    fun setProduct(ui: UiProduct?) {
        if (ui != null) {
            setState(FilledState(ui))
        } else {
            setState(EmptyState())
        }
    }
    // 도우미: 상태들이 호출할 액션
    fun showLoading() {
        binding.loadingOverlay.root.visibility = View.VISIBLE
    }
    fun hideLoading() {
        binding.loadingOverlay.root.visibility = View.GONE
    }
    //상품 있을 시 지우고 상태 업뎃
    fun clearCart() {
        currentProductId = -1
        setState(EmptyState()) }
    //상품 없을 시 구매하기로 이동
    fun navigateToSearch() {
        val action = CartFragmentDirections.actionCartFragmentToSearchFragment()
        navigator(action)
    }
}

