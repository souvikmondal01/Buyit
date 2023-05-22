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
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.utils.Constant.BUNDLE_KEY
import com.buyit.buyit.utils.Constant.BUNDLE_KEY_ID

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

        binding.cvBackArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val category = arguments?.getString(BUNDLE_KEY)
        val shopId = arguments?.getString(BUNDLE_KEY_ID)
        binding.tvCategoryName.text = category

        viewModel.fetchProductByCategory(shopId.toString(), category.toString())

        viewModel.productByCategoryList.observe(viewLifecycleOwner) {
            adapter = ProductByCategoryAdapter(it)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.notifyDataSetChanged()
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}