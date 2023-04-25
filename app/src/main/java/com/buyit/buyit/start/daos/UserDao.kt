package com.buyit.buyit.start.daos

import com.buyit.buyit.start.models.User
import com.buyit.buyit.utils.CommonUtils.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val userCollection = db.collection("user").document("customer").collection("user")

    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.id.toString()).set(it)
            }
        }
    }

}