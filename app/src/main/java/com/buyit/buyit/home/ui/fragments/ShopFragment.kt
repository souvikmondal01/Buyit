package com.buyit.buyit.home.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentShopBinding
import com.buyit.buyit.home.adapters.ProductAdapter
import com.buyit.buyit.home.adapters.ProductAdapterListener
import com.buyit.buyit.home.adapters.ProductChildAdapter
import com.buyit.buyit.home.adapters.ProductListener
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.utils.Constant.SHOP_ID
import com.buyit.buyit.utils.Constant.SHOP_NAME
import com.buyit.buyit.utils.Constant.SPF
import com.buyit.buyit.utils.hide
import com.buyit.buyit.utils.invisible
import com.buyit.buyit.utils.setStatusBarColor
import com.buyit.buyit.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShopFragment : Fragment(), ProductListener, ProductAdapterListener {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val factory = HomeViewModelFactory(HomeRepositoryImp())
    private lateinit var adapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.hide()
        setStatusBarColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

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
        }
        binding.cvCategory.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_shopCategoryFragment)
        }
        val sharedPreferences =
            requireActivity().getSharedPreferences(SPF, Context.MODE_PRIVATE)
        val shopName = sharedPreferences.getString(SHOP_NAME, "")
        val shopId = sharedPreferences.getString(SHOP_ID, "")
        binding.tvShopName.text = shopName

        viewModel.fetchProduct(shopId.toString())
        viewModel.productList.observe(viewLifecycleOwner) { list ->
            adapter = ProductAdapter(requireContext(), list, this, this)
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAddClick(holder: ProductChildAdapter.ViewHolder) {
        viewModel.increment()
        holder.binding.apply {
            btnAdd.invisible()
            btnPlus.show()
            btnMinus.show()
            tvCount.show()
            tvCount.text = viewModel.count.toString()
        }
    }

    override fun onPlusClick(holder: ProductChildAdapter.ViewHolder) {
        viewModel.increment()
        holder.apply {
            binding.apply {
                tvCount.text = viewModel.count.toString()
            }
        }
    }

    override fun onMinusClick(holder: ProductChildAdapter.ViewHolder) {
        viewModel.decrement()
        holder.apply {
            binding.apply {
                if (viewModel.count <= 0) {
                    btnAdd.show()
                    btnPlus.invisible()
                    btnMinus.invisible()
                    tvCount.invisible()
                } else {
                    tvCount.text = viewModel.count.toString()
                }
            }
        }
    }

    override fun onViewAllClick() {
        findNavController().navigate(R.id.action_shopFragment_to_productByCategoryFragment)
    }


}