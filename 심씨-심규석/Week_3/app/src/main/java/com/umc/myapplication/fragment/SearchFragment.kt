package com.umc.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.findNavController
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentSearchBinding
import com.umc.myapplication.databinding.ViewItemProductBinding
import com.umc.myapplication.model.SearchItem

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    val searchList = listOf<SearchItem>(
        SearchItem(
            productId = 1,
            resId = R.drawable.img_search_item1,
            name ="Nike Everyday Plus Cushioned",
            isWishList = true,
            description = "Training Ankle Socks (6 Pairs)",
            colors = 5,
            price = 10),
        SearchItem(
            productId = 2,
            resId = R.drawable.img_search_item1,
            name ="Nike Everyday Plus Cdddushioned",
            isWishList = false,
            description = "Training Ankle Sockds (6 Pairs)",
            colors = 200,
            price = 300),
    )

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        //searchList만큼 item생성 -> api로 변경할 예정
        searchList.mapIndexedNotNull { index, item ->
            val itemBinding = ViewItemProductBinding.inflate(layoutInflater, binding.grid, false)
            val isWishIcon = itemBinding.isWishListIcon

            //속성 전달
            itemBinding.image.setImageResource(item.resId)
            itemBinding.title.text = item.name
            itemBinding.description.text = item.description
            itemBinding.colors.text = "${item.colors}Colors"
            itemBinding.price.text = "US$${item.price}"
            isWishIcon.setImageResource(
                if (item.isWishList) R.drawable.ic_red_heart
                else R.drawable.ic_bnv_wish_list)

            //grid설정
            val lp = (itemBinding.root.layoutParams as GridLayout.LayoutParams).apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            }
            itemBinding.root.layoutParams = lp

            //이벤트 추가
            itemBinding.root.setOnClickListener {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToProductDetailFragment(
                        isWishList = item.isWishList,
                        productId = item.productId // 필요 시
                    )
                findNavController().navigate(action)
            }
            itemBinding.isWishListIcon.setOnClickListener { view ->
                val isWishList = !item.isWishList
                updateProductWishUi(isWishIcon,item.productId, isWishList)

            }
            findNavController().currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<Boolean>("isWishList_result_${item.productId}")
                ?.observe(viewLifecycleOwner) { newValue ->
                    // 1) 내부 데이터 업데이트
                    // 예: 어댑터 리스트라면 diffUtil로 갱신
                    // 2) 해당 itemView만 부분 갱신
                    updateProductWishUi(isWishIcon, item.productId, newValue)
                    // 3) 단발성 처리 후 제거(중복 관찰 방지)
                    findNavController().currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<Boolean>("isWishList_result_${item.productId}")
                }
            binding.grid.addView(itemBinding.root)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateProductWishUi(iconView : ImageView, productId: Int, isWishList: Boolean) {
        searchList.get(productId - 1).isWishList = isWishList
        iconView.setImageResource(if (isWishList) R.drawable.ic_red_heart else R.drawable.ic_bnv_wish_list)
    }

}