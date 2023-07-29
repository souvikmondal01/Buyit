package com.buyit.buyit.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.buyit.buyit.databinding.FragmentCartBinding
import com.buyit.buyit.home.adapters.CartAdapter
import com.buyit.buyit.home.adapters.CartItemAdapter
import com.buyit.buyit.home.adapters.CartListener
import com.buyit.buyit.home.models.Cart
import com.buyit.buyit.home.models.Product
import com.buyit.buyit.utils.CommonUtils.auth
import com.buyit.buyit.utils.CommonUtils.db
import com.buyit.buyit.utils.Constant.CUSTOMER
import com.buyit.buyit.utils.Constant.ORDER_LIST
import com.buyit.buyit.utils.Constant.PRODUCT
import com.buyit.buyit.utils.Constant.USER
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CartFragment : Fragment(), CartListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartAdapter
    private lateinit var cartItemAdapter: CartItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collection =
            db.collection(USER).document(CUSTOMER).collection(USER).document(auth.currentUser!!.uid)
                .collection(ORDER_LIST)


        val recyclerOptions = FirestoreRecyclerOptions.Builder<Cart>()
            .setQuery(collection, Cart::class.java).build()



        adapter = CartAdapter(recyclerOptions, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.startListening()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setProductsRecyclerView(holder: CartAdapter.ViewHolder, model: Cart) {
        val collectionProduct =
            db.collection(USER).document(CUSTOMER).collection(USER).document(auth.currentUser!!.uid)
                .collection(ORDER_LIST).document(model.shopId.toString()).collection(PRODUCT)

        val recyclerOptionsItems = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(collectionProduct, Product::class.java).build()
        cartItemAdapter = CartItemAdapter(recyclerOptionsItems)
        holder.binding.recyclerView.adapter = cartItemAdapter
        holder.binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartItemAdapter.startListening()
    }


}