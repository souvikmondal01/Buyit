package com.buyit.buyit.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentCategoryBinding
import com.buyit.buyit.home.adapters.CategoryAdapter
import com.buyit.buyit.home.models.Category
import com.buyit.buyit.utils.CommonUtils.db
import com.buyit.buyit.utils.Constant.CATEGORY_NAME
import com.buyit.buyit.utils.Constant.KEY
import com.buyit.buyit.utils.show
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryFragment : Fragment(), CategoryAdapter.OnCliCkListener {
    private lateinit var adapter: CategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCategoryBinding.inflate(inflater, container, false)

        val collection = db.collection("more").document("shopCategory").collection("list")
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Category>()
            .setQuery(collection, Category::class.java).build()
        adapter = CategoryAdapter(recyclerOptions, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter.startListening()

        binding.cvBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.show()
    }
    override fun onClick(category: Category) {
        findNavController().navigate(R.id.action_categoryFragment_to_shopByCategoryFragment)
        setFragmentResult(KEY, bundleOf(CATEGORY_NAME to category.categoryName))
    }

}