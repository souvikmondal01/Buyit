package com.buyit.buyit.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListProductByCategoryBinding
import com.buyit.buyit.home.models.Product

class ProductByCategoryAdapter(val list: List<Product>) :
    RecyclerView.Adapter<ProductByCategoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListProductByCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListProductByCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            binding.apply {
                tvProductName.text = data.name
                tvProductPrice.text = "₹" + data.price
                tvProductQuantity.text = data.quantity + " " + data.unit
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}