package com.buyit.buyit.home.repositories

import com.buyit.buyit.home.models.Product
import com.buyit.buyit.home.models.ProductCategory
import com.buyit.buyit.home.models.Shop
import com.buyit.buyit.start.models.Location
import com.firebase.ui.firestore.FirestoreRecyclerOptions

interface HomeRepository {
    fun getShopList(
        result: (List<Shop>) -> Unit,
        msg: (String) -> Unit
    ): FirestoreRecyclerOptions<Shop>

    fun getShopListByCategory(
        category: String,
        result: (List<Shop>) -> Unit,
        msg: (String) -> Unit
    ): FirestoreRecyclerOptions<Shop>

    fun getLocation(id: String, result: (Location?) -> Unit)
    fun fetchProduct(shopId: String, result: (ArrayList<ProductCategory>) -> Unit)
    fun fetchProductCategory(shopId: String, result: (ArrayList<ProductCategory>) -> Unit)

    fun fetchProductByCategory(
        shopId: String,
        category: String,
        result: (ArrayList<Product>) -> Unit
    )

}