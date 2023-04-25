package com.buyit.buyit.home.ui.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.buyit.buyit.databinding.FragmentHomeBinding
import com.buyit.buyit.home.adapters.ShopAdapter
import com.buyit.buyit.home.interfaces.HomeListener
import com.buyit.buyit.home.interfaces.ShopListener
import com.buyit.buyit.home.models.Shop
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.profile.ui.activities.ProfileActivity
import com.buyit.buyit.utils.CommonUtils.db
import com.buyit.buyit.utils.Constant.CLOSE
import com.buyit.buyit.utils.Constant.OPEN
import com.buyit.buyit.utils.Constant.SHOP
import com.buyit.buyit.utils.Constant.SHOP_ID
import com.buyit.buyit.utils.Constant.SHOP_NAME
import com.buyit.buyit.utils.Constant.SPF
import com.buyit.buyit.utils.Constant.STATUS
import com.buyit.buyit.utils.fragmentTo
import com.buyit.buyit.utils.setColourFilter
import com.buyit.buyit.utils.setStatusBarColor
import com.buyit.buyit.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment(), ShopListener, HomeListener {
    private lateinit var adapter: ShopAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val factory = HomeViewModelFactory(HomeRepositoryImp())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.homeListener = this
        binding.lifecycleOwner = this
        setStatusBarColor(R.color.orange_700)
        requireActivity().window.decorView.systemUiVisibility =
            View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getShopList()
        showShopList()
        viewModel.getLocation()

    }

    private fun showShopList() {
        viewModel.shopList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvEmpty.show()
                return@observe
            }
            viewModel.recyclerOptions.observe(viewLifecycleOwner) { recyclerOptions ->
                adapter = ShopAdapter(requireContext(), recyclerOptions, this)
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

    override fun onStart() {
        super.onStart()
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.show()
    }

    override fun onShopClick(shop: Shop) {
        findNavController().navigate(R.id.action_homeFragment_to_shopFragment)

        val sharedPreferences =
            requireActivity().getSharedPreferences(SPF, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(SHOP_ID, shop.id)
        editor.putString(SHOP_NAME, shop.shopName)
        editor.apply()

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
        db.collection(SHOP).document(model.id.toString())
            .addSnapshotListener { it, _ ->
                if (it!!.get(STATUS).toString() == OPEN) {
                    holder.binding.ivDot.setColourFilter(R.color.green)
                } else if (it.get(STATUS).toString() == CLOSE) {
                    holder.binding.ivDot.setColourFilter(R.color.red_light)
                } else {
                    holder.binding.ivDot.setColourFilter(R.color.grey)
                }
            }
    }

    override fun onAccountClick() {
        fragmentTo(ProfileActivity())
    }

    override fun onLocationClick() {
    }

    override fun onSearchClick() {
        findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
    }

}