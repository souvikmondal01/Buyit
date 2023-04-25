package com.buyit.buyit.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentShopByCategoryBinding
import com.buyit.buyit.home.adapters.ShopAdapter
import com.buyit.buyit.home.interfaces.ShopListener
import com.buyit.buyit.home.models.Shop
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.utils.CommonUtils.db
import com.buyit.buyit.utils.Constant
import com.buyit.buyit.utils.Constant.KEY
import com.buyit.buyit.utils.Constant.KEY_SHOP
import com.buyit.buyit.utils.Constant.SHOP_NAME
import com.buyit.buyit.utils.hide
import com.buyit.buyit.utils.setColourFilter
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShopByCategoryFragment : Fragment(), ShopListener {

    private var _binding: FragmentShopByCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShopAdapter
    private lateinit var viewModel: HomeViewModel
    private val factory = HomeViewModelFactory(HomeRepositoryImp())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopByCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvBackArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setFragmentResultListener(KEY) { _, bundle ->
            val categoryName = bundle.getString(Constant.CATEGORY_NAME).toString()
            viewModel.getShopListByCategory(categoryName)
            viewModel.recyclerOptionsCategory.observe(viewLifecycleOwner) {
                adapter = ShopAdapter(requireContext(), it, this)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                adapter.startListening()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onShopClick(shop: Shop) {
        findNavController().navigate(R.id.action_shopByCategoryFragment_to_shopFragment)
        setFragmentResult(KEY_SHOP, bundleOf(SHOP_NAME to shop.shopName, Constant.ID to shop.id))
    }

    override fun onFavouriteClick(holder: ShopAdapter.ViewHolder) {
        if (viewModel.isFavourite) {
            holder.binding.ivFavorite.setImageResource(R.drawable.favorite)
        } else {
            holder.binding.ivFavorite.setImageResource(R.drawable.favorite_border)
        }
        viewModel.isFavourite = !viewModel.isFavourite
    }

    override fun setShopStatus(holder: ShopAdapter.ViewHolder, position: Int, model: Shop) {
        db.collection(Constant.SHOP).document(model.id.toString())
            .addSnapshotListener { it, _ ->
                if (it!!.get(Constant.STATUS).toString() == Constant.OPEN) {
                    holder.binding.ivDot.setColourFilter(R.color.green)
                } else if (it.get(Constant.STATUS).toString() == Constant.CLOSE) {
                    holder.binding.ivDot.setColourFilter(R.color.red_light)
                } else {
                    holder.binding.ivDot.setColourFilter(R.color.grey)
                }
            }
    }

}