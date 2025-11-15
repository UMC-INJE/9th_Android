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
    private val onItemClick: (UiProduct) -> Unit,
    private val onToggleWish: (UiProduct) -> Unit
) : ListAdapter<UiProduct, SearchUiProductAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<UiProduct>() {
        override fun areItemsTheSame(o: UiProduct, n: UiProduct) = o.id == n.id

        override fun areContentsTheSame(o: UiProduct, n: UiProduct) = o == n

        override fun getChangePayload(o: UiProduct, n: UiProduct) = o.liked != n.liked

    }

    inner class VH(private val b: ViewItemProductBinding) : RecyclerView.ViewHolder(b.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: UiProduct) {
            b.resId.setImageResource(item.imageResource)
            b.title.text = item.name
            b.description.text = item.shortDescription
            b.colors.text = "${item.colors}Colors"
            b.price.text = "US$${item.price}"
            b.isWishListIcon.setWishIcon(item.liked)
            b.root.setOnClickListener { onItemClick(item) }
            b.wishListButton.setOnClickListener { onToggleWish(item) }
        }
        fun bindWishOnly(isLiked: Boolean) {
            b.isWishListIcon.setWishIcon(isLiked)
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, viewType: Int): VH {
        val b = ViewItemProductBinding.inflate(LayoutInflater.from(p.context), p, false)
        return VH(b)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        h.bind(getItem(pos))
    }

    override fun onBindViewHolder(h: VH, pos: Int, payloads: MutableList<Any>) {
        if (payloads.contains("wish")) {
            h.bindWishOnly(getItem(pos).liked)
            h.bind(getItem(pos))
        } else super.onBindViewHolder(h, pos, payloads)
    }
}
