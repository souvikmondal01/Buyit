package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListProductChildBinding

class ProductChildAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<ProductChildAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListProductChildBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListProductChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            binding.tvProductName.text = data

            itemView.setOnClickListener { }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}