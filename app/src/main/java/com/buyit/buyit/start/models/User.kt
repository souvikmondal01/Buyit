package com.buyit.buyit.start.models

data class User(
    var id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val createdAt: String? = null,
    val androidID: String? = null,
    val photoUrl: String? = null,
    val location: Location? = null,
    val likedShop: ArrayList<String> = arrayListOf(),
)

