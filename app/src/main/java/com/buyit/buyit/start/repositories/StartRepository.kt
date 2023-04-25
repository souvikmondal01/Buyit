package com.buyit.buyit.start.repositories

import com.buyit.buyit.start.models.User


interface StartRepository {
    fun registerUser(user: User?, result: (String) -> Unit)
    fun setUser(user: User?)
    fun loginUser(user: User?, result: (String) -> Unit)
    fun resetPassword(email: String?, result: (String) -> Unit)
    fun googleSignIn(result: (String) -> Unit)
}