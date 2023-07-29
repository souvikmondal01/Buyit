package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListCartBinding
import com.buyit.buyit.home.models.Cart
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CartAdapter(
    option: FirestoreRecyclerOptions<Cart>,
    private val listener: CartListener
) : FirestoreRecyclerAdapter<Cart, CartAdapter.ViewHolder>(option) {
    class ViewHolder(val binding: ListCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Cart) {
        listener.setProductsRecyclerView(holder, model)
        holder.apply {
            binding.tvShopName.text = model.shopName
        }
    }
}

interface CartListener {
    fun setProductsRecyclerView(holder: CartAdapter.ViewHolder, model: Cart)
}