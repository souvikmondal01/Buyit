package com.buyit.buyit.home.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentProductCategoryBinding
import com.buyit.buyit.home.adapters.ProductCategoryAdapter
import com.buyit.buyit.utils.Constant
import com.buyit.buyit.utils.hide
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductCategoryFragment : Fragment() {
    private var _binding: FragmentProductCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_category, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.cvSearch.setOnClickListener {}
        binding.cvHome.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.cvCategory.setOnClickListener {}

        val sharedPreferences =
            requireActivity().getSharedPreferences(Constant.SPF, Context.MODE_PRIVATE)
        val shopName = sharedPreferences.getString(Constant.SHOP_NAME, "")
        binding.tvShopName.text = shopName

        val list = ArrayList<String>()
        list.add("Category")
        list.add("Category")
        list.add("Category")
        list.add("Category")
        list.add("Category")
        list.add("Category")
        list.add("Category")
        list.add("Category")


        adapter = ProductCategoryAdapter(list)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}