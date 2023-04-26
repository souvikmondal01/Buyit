package com.buyit.buyit.home.models

data class ProductCategory(
    val id: String? = null,
    val category: String? = null,
    val url: String? = null,
    val product: ArrayList<Product>? = null
)
