package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListSearchBinding
import com.buyit.buyit.home.interfaces.ShopListener
import com.buyit.buyit.home.models.Shop

class SearchAdapter(private var list: List<Shop>, private val listener: ShopListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shop = list[position]
        holder.binding.tvShopName.text = shop.shopName.toString()
        holder.binding.tvShopCategory.text = shop.shopCategory.toString()
        holder.itemView.setOnClickListener {
            listener.onShopClick(shop)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setFilteredList(list: List<Shop>) {
        this.list = list
        notifyDataSetChanged()
    }

}