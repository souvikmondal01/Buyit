package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListCartItemBinding
import com.buyit.buyit.home.models.Product
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CartItemAdapter(option: FirestoreRecyclerOptions<Product>) :
    FirestoreRecyclerAdapter<Product, CartItemAdapter.ViewHolder>(option) {
    class ViewHolder(val binding: ListCartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {
        holder.apply {
            binding.apply {
                tvProductName.text = model.name
                tvProductCount.text = model.quantity
                tvProductPrice.text = "₹ ${model.price}"
                tvProductQuantity.text = ""
            }

        }
    }
}