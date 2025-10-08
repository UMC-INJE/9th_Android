package com.umc.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.R
import com.umc.myapplication.databinding.ViewItemNewProductBinding
import com.umc.myapplication.databinding.ViewItemProductBinding
import com.umc.myapplication.databinding.ViewWishListItemBinding
import com.umc.myapplication.model.Product

class WishListProductAdapter(var newProductList: List<Product>, val onItemclick: (Product) -> Unit) : RecyclerView.Adapter<WishListProductAdapter.ViewHolder>() {
    inner class ViewHolder (val binding : ViewWishListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data : Product){
            binding.resId.setImageResource(data.resId)
            binding.name.text = data.name
            binding.shortDescription.text = data.shortDescription
            binding.price.text = "US$${data.price}"
            binding.root.setOnClickListener {
                onItemclick(data)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ViewWishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

