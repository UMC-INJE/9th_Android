package com.umc.myapplication.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ViewWishListItemBinding
import com.umc.myapplication.domain.model.UiProduct

class WishListProductAdapter(
    val onItemclick: (UiProduct) -> Unit
) : ListAdapter<UiProduct, WishListProductAdapter.ViewHolder>(ProductDiffCallback()) {

    inner class ViewHolder(val binding: ViewWishListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: UiProduct) {
            binding.resId.setImageResource(data.imageResource)
            binding.name.text = data.name
            binding.shortDescription.text = data.shortDescription
            binding.price.text = "US$${data.price}"
            binding.root.setOnClickListener {
                onItemclick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewWishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nowNewProduct = getItem(position)
        holder.bind(nowNewProduct)
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<UiProduct>() {
        override fun areItemsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean =
            oldItem == newItem
    }
}