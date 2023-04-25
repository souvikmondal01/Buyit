package com.buyit.buyit.home.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentSearchBinding
import com.buyit.buyit.home.adapters.SearchAdapter
import com.buyit.buyit.home.adapters.ShopAdapter
import com.buyit.buyit.home.interfaces.ShopListener
import com.buyit.buyit.home.models.Shop
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.utils.Constant
import com.buyit.buyit.utils.hide
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class SearchFragment : Fragment(), ShopListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val factory = HomeViewModelFactory(HomeRepositoryImp())
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search,
            container, false
        )
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.requestFocus()

        viewModel.getShopList()
        viewModel.shopList.observe(viewLifecycleOwner) {
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter = SearchAdapter(listOf(), this)
            binding.recyclerView.adapter = adapter
            binding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query != null) {
                        val filterList = ArrayList<Shop>()
                        for (i in it) {
                            if (i.shopName?.lowercase(Locale.ROOT)!!
                                    .contains(query) || i.shopCategory?.lowercase(Locale.ROOT)!!
                                    .contains(query)
                            ) {
                                filterList.add(i)
                            }
                        }
                        if (filterList.isEmpty()) {
                            adapter.setFilteredList(listOf())
                        } else {
                            adapter.setFilteredList(filterList)
                        }
                    }
                    if (query.isNullOrEmpty()) {
                        adapter.setFilteredList(listOf())
                    }
                    return true
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onShopClick(shop: Shop) {
        findNavController().navigate(R.id.action_searchFragment_to_shopFragment)

        val sharedPreferences =
            requireActivity().getSharedPreferences(Constant.SPF, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constant.SHOP_ID, shop.id)
        editor.putString(Constant.SHOP_NAME, shop.shopName)
        editor.apply()
    }

    override fun onFavouriteClick(holder: ShopAdapter.ViewHolder) {

    }

    override fun setShopStatus(holder: ShopAdapter.ViewHolder, position: Int, model: Shop) {

    }


}
