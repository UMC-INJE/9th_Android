package com.umc.myapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.myapplication.databinding.ViewItemNewProductBinding
import com.umc.myapplication.domain.model.UiProduct

class HomeNewProductAdapter(
    private val onItemclick: (UiProduct) -> Unit
) : ListAdapter<UiProduct, HomeNewProductAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<UiProduct>() {
        override fun areItemsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean {
            // 고유 id 비교
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean {
            // 내용 전체 비교 (데이터 클래스면 == 로 충분)
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ViewItemNewProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UiProduct) {
            binding.resId.setImageResource(data.imageResource)
            binding.title.text = data.name
            binding.price.text = "US$${data.price}"
            binding.root.setOnClickListener { onItemclick(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewItemNewProductBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
