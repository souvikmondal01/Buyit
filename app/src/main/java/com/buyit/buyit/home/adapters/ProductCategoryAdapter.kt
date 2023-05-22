package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListProductCategoryBinding
import com.buyit.buyit.home.models.ProductCategory

class ProductCategoryAdapter(
    val list: ArrayList<ProductCategory>,
    val listener: ProductCategoryListener
) :
    RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListProductCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            binding.apply {
                tvProductCategory.text = data.category

            }
            itemView.setOnClickListener {
                listener.onCategoryClick(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

interface ProductCategoryListener {
    fun onCategoryClick(productCategory: ProductCategory)
}