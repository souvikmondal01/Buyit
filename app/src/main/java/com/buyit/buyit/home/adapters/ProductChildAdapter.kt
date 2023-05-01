package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListProductChildBinding
import com.buyit.buyit.home.models.Product

class ProductChildAdapter(private val list: ArrayList<Product>?, val listener: ProductListener) :
    RecyclerView.Adapter<ProductChildAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListProductChildBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListProductChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list?.get(position)
        listener.productClicks(holder, data!!)


    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

interface ProductListener {
    fun productClicks(holder: ProductChildAdapter.ViewHolder, data: Product)
}