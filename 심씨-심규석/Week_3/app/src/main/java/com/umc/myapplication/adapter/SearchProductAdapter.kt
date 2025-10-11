package com.umc.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.R
import com.umc.myapplication.databinding.ViewItemNewProductBinding
import com.umc.myapplication.databinding.ViewItemProductBinding
import com.umc.myapplication.model.Product

class SearchProductAdapter(var newProductList: List<Product>, val onItemclick: (Product) -> Unit) : RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {
    inner class ViewHolder (val binding : ViewItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data : Product){
            binding.resId.setImageResource(data.resId)
            binding.title.text = data.name
            binding.price.text = "US$${data.price}"
            binding.description.text = data.shortDescription
            binding.colors.text = "${data.colors}Colors"
            binding.isWishListIcon.setImageResource(if (data.isWishList) R.drawable.ic_red_heart else R.drawable.ic_bnv_wish_list)
            binding.root.setOnClickListener {
                onItemclick(data)
            }
            binding.wishListButton.setOnClickListener {
                data.isWishList = !data.isWishList
                binding.isWishListIcon.setImageResource(if (data.isWishList) R.drawable.ic_red_heart else R.drawable.ic_bnv_wish_list)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ViewItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val nowNewProduct = newProductList[position]
        holder.bind(nowNewProduct)
    }

    override fun getItemCount(): Int {
        return newProductList.size
    }

}

