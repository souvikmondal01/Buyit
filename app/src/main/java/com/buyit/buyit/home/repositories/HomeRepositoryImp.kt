package com.buyit.buyit.home.repositories

import com.buyit.buyit.home.models.Shop
import com.buyit.buyit.start.models.Location
import com.buyit.buyit.start.models.User
import com.buyit.buyit.utils.CommonUtils.db
import com.buyit.buyit.utils.Constant
import com.buyit.buyit.utils.Constant.CLOSE
import com.buyit.buyit.utils.Constant.DONE
import com.buyit.buyit.utils.Constant.OPEN
import com.buyit.buyit.utils.Constant.SHOP
import com.buyit.buyit.utils.Constant.SHOP_CATEGORY
import com.buyit.buyit.utils.Constant.STATUS
import com.buyit.buyit.utils.Constant.SUCCESS
import com.buyit.buyit.utils.Constant.VERIFICATION_STATUS
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeRepositoryImp : HomeRepository {
    private val shopRef = db.collection(SHOP).whereEqualTo(VERIFICATION_STATUS, DONE)
        .whereIn(STATUS, listOf(OPEN, CLOSE))

    override fun getShopList(
        result: (List<Shop>) -> Unit,
        msg: (String) -> Unit
    ): FirestoreRecyclerOptions<Shop> {
        GlobalScope.launch {
            try {
                val shops = shopRef.get().await()
                    .toObjects(Shop::class.java)
                withContext(Dispatchers.Main) {
                    result.invoke(shops)
                    msg.invoke(SUCCESS)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    msg.invoke(e.message.toString())
                }
            }
        }

        return FirestoreRecyclerOptions.Builder<Shop>().setQuery(shopRef, Shop::class.java).build()
    }

    override fun getShopListByCategory(
        category: String,
        result: (List<Shop>) -> Unit,
        msg: (String) -> Unit
    ): FirestoreRecyclerOptions<Shop> {
        val ref = shopRef.whereEqualTo(SHOP_CATEGORY, category)
        return FirestoreRecyclerOptions.Builder<Shop>().setQuery(ref, Shop::class.java).build()
    }

    override fun getLocation(id: String, result: (Location?) -> Unit) {
//        GlobalScope.launch {
//            val user =
//                db.collection(Constant.USER).document(Constant.CUSTOMER).collection(Constant.USER)
//                    .document(CommonUtils.auth.currentUser!!.uid).get().await()
//                    .toObject(User::class.java)
//            val location: Location? = user?.location
//            withContext(Dispatchers.Main) {
//                result.invoke(location)
//            }
//        }

        db.collection(Constant.USER).document(Constant.CUSTOMER).collection(Constant.USER)
            .document(id).addSnapshotListener { it, _ ->
                val user = it?.toObject(User::class.java)
                val location = user?.location
                result.invoke(location)
            }
    }


}