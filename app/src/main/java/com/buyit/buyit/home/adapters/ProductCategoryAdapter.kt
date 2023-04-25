package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListProductCategoryBinding

class ProductCategoryAdapter(val list: ArrayList<String>) :
    RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListProductCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[0]
        holder.apply {
            binding.apply {
                tvProductCategory.text = data
            }
            itemView.setOnClickListener { }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}