package com.buyit.buyit.home.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.buyit.buyit.home.models.Product
import com.buyit.buyit.home.models.ProductCategory
import com.buyit.buyit.home.repositories.HomeRepositoryImp
import com.buyit.buyit.home.viewModels.HomeViewModel
import com.buyit.buyit.home.viewModels.HomeViewModelFactory
import com.buyit.buyit.utils.CommonUtils
import com.buyit.buyit.utils.Constant
import com.buyit.buyit.utils.Constant.BUNDLE_KEY
import com.buyit.buyit.utils.Constant.BUNDLE_KEY_ID
import com.buyit.buyit.utils.Constant.PRODUCT
import com.buyit.buyit.utils.Constant.QUANTITY
import com.buyit.buyit.utils.Constant.SHOP_ID
import com.buyit.buyit.utils.Constant.SHOP_NAME
import com.buyit.buyit.utils.Constant.SPF
import com.buyit.buyit.utils.Constant.STATUS
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

    @SuppressLint("SetTextI18n")
    override fun productClicks(holder: ProductChildAdapter.ViewHolder, data: Product) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(SPF, Context.MODE_PRIVATE)
        val shopName = sharedPreferences.getString(SHOP_NAME, "")
        val shopId = sharedPreferences.getString(SHOP_ID, "")

        CommonUtils.db.collection(Constant.USER).document(Constant.CUSTOMER)
            .collection(Constant.USER).document(viewModel.id)
            .collection(Constant.ORDER_LIST)
            .document(shopId.toString()).collection(PRODUCT).document(data.id.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    holder.binding.apply {
                        btnAdd.invisible()
                        btnPlus.show()
                        btnMinus.show()
                        tvCount.show()
                        tvCount.text = document.data!![QUANTITY].toString()
//                        viewModel.setValue(document.data!![QUANTITY].toString().toInt())
                    }
                } else {
                    Log.d("LOG", "No such document")
                }
            }

        holder.apply {
            binding.apply {
                tvProductName.text = data.name
                tvProductPrice.text = "₹${data.price}"
                tvProductQuantity.text = data.quantity + " " + data.unit

                btnAdd.setOnClickListener {
                    viewModel.reset()
                    viewModel.increment()
                    holder.binding.apply {
                        btnAdd.invisible()
                        btnPlus.show()
                        btnMinus.show()
                        tvCount.show()
                        tvCount.text = viewModel.count.toString()
                    }

                    val dbRef = CommonUtils.db.collection(Constant.USER).document(Constant.CUSTOMER)
                        .collection(Constant.USER).document(viewModel.id)
                        .collection(Constant.ORDER_LIST)
                        .document(shopId.toString())
                    dbRef.collection(PRODUCT).document(data.id.toString())
                        .set(Product(data.id, data.name, data.price, viewModel.count.toString()))
                    dbRef.set(
                        hashMapOf(
                            SHOP_ID to shopId,
                            SHOP_NAME to shopName,
                            STATUS to "added"
                        )
                    )
                }

                btnPlus.setOnClickListener {

                    CommonUtils.db.collection(Constant.USER).document(Constant.CUSTOMER)
                        .collection(Constant.USER).document(viewModel.id)
                        .collection(Constant.ORDER_LIST)
                        .document(shopId.toString()).collection(PRODUCT)
                        .document(data.id.toString()).get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                viewModel.setValue(document.data!![QUANTITY].toString().toInt())
                                viewModel.increment()
                                holder.apply {
                                    binding.apply {
                                        tvCount.text = viewModel.count.toString()
                                    }
                                }
                                CommonUtils.db.collection(Constant.USER).document(Constant.CUSTOMER)
                                    .collection(Constant.USER).document(viewModel.id)
                                    .collection(Constant.ORDER_LIST)
                                    .document(shopId.toString()).collection(PRODUCT)
                                    .document(data.id.toString())
                                    .update(QUANTITY, viewModel.count.toString())
                            } else {
                                Log.d("LOG", "No such document")
                            }
                        }

                }

                btnMinus.setOnClickListener {
                    CommonUtils.db.collection(Constant.USER).document(Constant.CUSTOMER)
                        .collection(Constant.USER).document(viewModel.id)
                        .collection(Constant.ORDER_LIST)
                        .document(shopId.toString()).collection(PRODUCT)
                        .document(data.id.toString()).get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                viewModel.setValue(document.data!![QUANTITY].toString().toInt())

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

                                CommonUtils.db.collection(Constant.USER).document(Constant.CUSTOMER)
                                    .collection(Constant.USER).document(viewModel.id)
                                    .collection(Constant.ORDER_LIST)
                                    .document(shopId.toString()).collection(PRODUCT)
                                    .document(data.id.toString())
                                    .update(QUANTITY, viewModel.count.toString())
                                if (viewModel.count <= 0) {
                                    val ref = CommonUtils.db.collection(Constant.USER)
                                        .document(Constant.CUSTOMER)
                                        .collection(Constant.USER).document(viewModel.id)
                                        .collection(Constant.ORDER_LIST)
                                        .document(shopId.toString())
                                    ref.collection(PRODUCT)
                                        .document(data.id.toString()).delete()
                                    ref.delete()

                                }


                            } else {
                                Log.d("LOG", "No such document")
                            }
                        }


                }
            }

            itemView.setOnClickListener { }
        }
    }

    override fun onViewAllClick(productCategory: ProductCategory) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(SPF, Context.MODE_PRIVATE)
        val shopId = sharedPreferences.getString(SHOP_ID, "")

        val bundle = bundleOf(
            BUNDLE_KEY to productCategory.category.toString(),
            BUNDLE_KEY_ID to shopId
        )
        findNavController().navigate(R.id.action_shopFragment_to_productByCategoryFragment, bundle)
    }


}
