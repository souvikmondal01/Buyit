package com.buyit.buyit.home.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentProductCategoryBinding
import com.buyit.buyit.home.adapters.ProductCategoryAdapter
import com.buyit.buyit.home.adapters.ProductCategoryListener
import com.buyit.buyit.home.models.ProductCategory
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.utils.Constant
import com.buyit.buyit.utils.Constant.SHOP_ID
import com.buyit.buyit.utils.Constant.SHOP_NAME
import com.buyit.buyit.utils.Constant.SPF
import com.buyit.buyit.utils.hide
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductCategoryFragment : Fragment(), ProductCategoryListener {
    private var _binding: FragmentProductCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductCategoryAdapter
    private lateinit var viewModel: HomeViewModel
    private val factory = HomeViewModelFactory(HomeRepositoryImp())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_category, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.hide()
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
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
            requireActivity().getSharedPreferences(SPF, Context.MODE_PRIVATE)
        val shopName = sharedPreferences.getString(SHOP_NAME, "")
        val shopId = sharedPreferences.getString(SHOP_ID, "")
        binding.tvShopName.text = shopName

        viewModel.fetchProductCategory(shopId.toString())
        viewModel.productCategoryList.observe(viewLifecycleOwner) { list ->
            adapter = ProductCategoryAdapter(list, this)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryClick(productCategory: ProductCategory) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(SPF, Context.MODE_PRIVATE)
        val shopId = sharedPreferences.getString(SHOP_ID, "")

        val bundle = bundleOf(
            Constant.BUNDLE_KEY to productCategory.category.toString(),
            Constant.BUNDLE_KEY_ID to shopId
        )
        findNavController().navigate(
            R.id.action_shopCategoryFragment_to_productByCategoryFragment,
            bundle
        )
    }

}