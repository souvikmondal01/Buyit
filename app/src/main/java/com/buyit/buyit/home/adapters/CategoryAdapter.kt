package com.buyit.buyit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buyit.buyit.databinding.ListCategoryBinding
import com.buyit.buyit.home.models.Category
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CategoryAdapter(
    option: FirestoreRecyclerOptions<Category>,
    private val listener: OnCliCkListener
) :
    FirestoreRecyclerAdapter<Category, CategoryAdapter.ViewHolder>(option) {
    class ViewHolder(val binding: ListCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Category) {
        holder.apply {
            binding.tvCategoryName.text = model.categoryName
            itemView.setOnClickListener {
                listener.onClick(model)
            }

        }

    }

    interface OnCliCkListener {
        fun onClick(category: Category)
    }

}

