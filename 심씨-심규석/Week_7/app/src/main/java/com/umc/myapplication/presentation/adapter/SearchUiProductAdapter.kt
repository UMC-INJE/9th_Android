package com.umc.myapplication.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ViewItemProductBinding
import com.umc.myapplication.domain.model.UiProduct
import com.umc.myapplication.presentation.utils.setWishIcon

class SearchUiProductAdapter(
    private val onItemclick: (UiProduct) -> Unit,
    private val onToggleWish: (UiProduct) -> Unit = {} // 외부에 상태 변경 위임(권장)
) : ListAdapter<UiProduct, SearchUiProductAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<UiProduct>() {
        override fun areItemsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean =
            oldItem.id == newItem.id // 고유 키 비교 [web:47][web:66]

        override fun areContentsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean =
            oldItem == newItem // data class면 ==로 내용 비교 가능 [web:55][web:41]

        // 선택: 위시 상태만 바뀐 경우 payload로 최적화 가능
        override fun getChangePayload(oldItem: UiProduct, newItem: UiProduct): Any? {
            return if (oldItem.isLiked != newItem.isLiked) {
                PAYLOAD_WISH
            } else null
        }
    }

    inner class ViewHolder(
        private val binding: ViewItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: UiProduct) {
            binding.resId.setImageResource(item.imageResource)
            binding.title.text = item.name
            binding.price.text = "US$${item.price}"
            binding.description.text = item.shortDescription
            binding.colors.text = "${item.colors}Colors"
            binding.isWishListIcon.setWishIcon(isWish = item.isLiked)
            binding.root.setOnClickListener { onItemclick(item) }
            binding.wishListButton.setOnClickListener {
                // 내부에서 모델을 mutate하지 말고 외부에 토글 이벤트를 위임
                onToggleWish(item)
            }
        }

        fun bindWishOnly(isLiked: Boolean) {
            binding.isWishListIcon.setWishIcon(isWish = isLiked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 선택: payload 최적화
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.contains(PAYLOAD_WISH)) {
            holder.bindWishOnly(getItem(position).isLiked)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    companion object {
        private const val PAYLOAD_WISH = "wish"
    }
}
