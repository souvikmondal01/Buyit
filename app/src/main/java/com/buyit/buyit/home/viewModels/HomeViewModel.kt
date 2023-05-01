package com.buyit.buyit.home.viewModels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buyit.buyit.home.interfaces.HomeListener
import com.buyit.buyit.home.models.ProductCategory
import com.buyit.buyit.home.models.Shop
import com.buyit.buyit.home.repositories.HomeRepository
import com.buyit.buyit.start.models.Location
import com.buyit.buyit.utils.CommonUtils.auth
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private var _shopList = MutableLiveData<List<Shop>>()
    val shopList get() = _shopList

    private var _msg = MutableLiveData<String>()
    val msg get() = _msg

    private var _recyclerOptions = MutableLiveData<FirestoreRecyclerOptions<Shop>>()
    val recyclerOptions get() = _recyclerOptions

    private var _recyclerOptionsCategory = MutableLiveData<FirestoreRecyclerOptions<Shop>>()
    val recyclerOptionsCategory get() = _recyclerOptionsCategory

    private var _location = MutableLiveData<Location>()
    val location get() = _location

    var loc = MutableLiveData("")

    var homeListener: HomeListener? = null
    var isFavourite = false
    val id = auth.currentUser!!.uid

    private var _productList = MutableLiveData<ArrayList<ProductCategory>>()
    val productList get() = _productList

    private var _productCategoryList = MutableLiveData<ArrayList<ProductCategory>>()
    val productCategoryList get() = _productCategoryList

    fun getShopList() {
        val options = repository.getShopList({
            _shopList.value = it
        },
            { _msg.value = it })
        _recyclerOptions.value = options
    }

    fun getLocation() {
        repository.getLocation(id) {
            _location.value = it
            loc.value = it?.city.toString() + ", " + it?.pin.toString()
        }
    }

    fun getShopListByCategory(category: String) {
        _recyclerOptionsCategory.value =
            repository.getShopListByCategory(category, {}, {
            })
    }

    fun onAccountClick(view: View) {
        homeListener?.onAccountClick()
    }

    fun onLocationClick(view: View) {
        homeListener?.onLocationClick()
    }

    fun onSearchClick(view: View) {
        homeListener?.onSearchClick()
    }

    fun fetchProduct(shopId: String) = repository.fetchProduct(shopId) {
        _productList.value = it
    }

    fun fetchProductCategory(shopId: String) = repository.fetchProductCategory(shopId) {
        _productCategoryList.value = it
    }


    var count = 0
    fun increment() {
        count++
    }

    fun decrement() {
        count--
    }

    fun reset() {
        count = 0
    }
    fun setValue(num: Int) {
        count = num
    }


}


