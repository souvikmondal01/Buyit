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
        holder.apply {
            if (data != null) {
                binding.apply {
                    tvProductName.text = data.name
                    tvProductPrice.text = "₹" + data.price
                    tvProductQuantity.text = data.quantity + " " + data.unit

                    btnAdd.setOnClickListener {
                        listener.onAddClick(holder)
                    }
                    btnPlus.setOnClickListener {
                        listener.onPlusClick(holder)
                    }
                    btnMinus.setOnClickListener {
                        listener.onMinusClick(holder)
                    }
                }
            }

            itemView.setOnClickListener { }
        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

interface ProductListener {
    fun onAddClick(holder: ProductChildAdapter.ViewHolder)
    fun onPlusClick(holder: ProductChildAdapter.ViewHolder)
    fun onMinusClick(holder: ProductChildAdapter.ViewHolder)
}