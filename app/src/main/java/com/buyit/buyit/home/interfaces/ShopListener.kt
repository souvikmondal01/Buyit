package com.buyit.buyit.home.interfaces

import com.buyit.buyit.home.adapters.ShopAdapter
import com.buyit.buyit.home.models.Shop

interface ShopListener {
    fun onShopClick(shop: Shop)
    fun onFavouriteClick(holder: ShopAdapter.ViewHolder)
    fun setShopStatus(holder: ShopAdapter.ViewHolder, position: Int, model: Shop)
}