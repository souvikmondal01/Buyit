package com.buyit.buyit.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListShopBinding
import com.buyit.buyit.home.interfaces.ShopListener
import com.buyit.buyit.home.models.Shop
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class ShopAdapter(
    val context: Context,
    options: FirestoreRecyclerOptions<Shop>,
    private val listener: ShopListener
) :
    FirestoreRecyclerAdapter<Shop, ShopAdapter.ViewHolder>(options) {

    class ViewHolder(val binding: ListShopBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Shop) {
        holder.apply {
            binding.apply {
                tvShopName.text = model.shopName
                tvShopCategory.text = model.shopCategory
                vFavorite.setOnClickListener {
                    listener.onFavouriteClick(holder)
                }
            }
            itemView.setOnClickListener {
                listener.onShopClick(model)
            }
            listener.setShopStatus(holder, position, model)
        }
    }


}