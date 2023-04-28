package com.buyit.buyit.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListProductBinding
import com.buyit.buyit.home.models.ProductCategory

class ProductAdapter(
    val context: Context,
    private val list: ArrayList<ProductCategory>, private val listener2: ProductListener,
    private val listener3: ProductAdapterListener
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>(), ProductListener {
    class ViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root)

    val listener: ProductListener = this

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val adapter = ProductChildAdapter(data.product, this)
        holder.apply {
            binding.apply {
                tvProductCategory.text = data.category
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

                btnViewAll.setOnClickListener {
                    listener3.onViewAllClick()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onAddClick(holder: ProductChildAdapter.ViewHolder) {
        listener2.onAddClick(holder)
    }

    override fun onPlusClick(holder: ProductChildAdapter.ViewHolder) {
        listener2.onPlusClick(holder)
    }

    override fun onMinusClick(holder: ProductChildAdapter.ViewHolder) {
        listener2.onMinusClick(holder)
    }
}

interface ProductAdapterListener {
    fun onViewAllClick()
}