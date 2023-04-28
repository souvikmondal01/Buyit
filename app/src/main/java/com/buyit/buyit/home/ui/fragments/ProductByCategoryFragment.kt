package com.buyit.buyit.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.buyit.buyit.databinding.FragmentProductByCategoryBinding
import com.buyit.buyit.home.adapters.ProductByCategoryAdapter
import com.buyit.buyit.home.models.Product
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory

class ProductByCategoryFragment : Fragment() {
    private var _binding: FragmentProductByCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val factory = HomeViewModelFactory(HomeRepositoryImp())
    private lateinit var adapter: ProductByCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductByCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: ArrayList<Product> = arrayListOf()
        list.add(Product("id", "name1", "100", "500", "g"))
        list.add(Product("id", "name2", "100", "500", "g"))
        list.add(Product("id", "name3", "100", "500", "g"))
        list.add(Product("id", "name4", "100", "500", "g"))
        list.add(Product("id", "name5", "100", "500", "g"))
        adapter = ProductByCategoryAdapter(list)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}