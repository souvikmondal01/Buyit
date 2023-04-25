package com.buyit.buyit.start.repositories

import com.buyit.buyit.start.models.User
import com.buyit.buyit.utils.CommonUtils.auth
import com.buyit.buyit.utils.CommonUtils.db
import com.buyit.buyit.utils.Constant.LOGIN_SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StartRepositoryImp : StartRepository {
    private val userCollection =
        db.collection("user").document("customer").collection("user")

    override fun registerUser(user: User?, result: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(
                    user!!.email.toString(),
                    user.password.toString()
                ).await()
                user.id = authResult.user!!.uid
                authResult.user!!.sendEmailVerification().await()
                setUser(user)
                withContext(Dispatchers.Main) {
                    result.invoke(
                        "Registration Successful.Please check your email for verification"
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(e.message.toString())
                }
            }
        }
    }

    override fun setUser(user: User?) {
        GlobalScope.launch(Dispatchers.IO) {
            user?.let {
                userCollection.document(user.id.toString()).set(it)
            }
        }
    }

    override fun loginUser(user: User?, result: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val authResult =
                    auth.signInWithEmailAndPassword(
                        user?.email.toString(),
                        user?.password.toString()
                    ).await()
                withContext(Dispatchers.Main) {
                    if (authResult.user!!.isEmailVerified) {
                        result.invoke(LOGIN_SUCCESS)
                        return@withContext
                    }
                    result.invoke("Email is not verified")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(
                        e.message.toString()
                    )
                }
            }
        }
    }

    override fun resetPassword(email: String?, result: (String) -> Unit) {
        GlobalScope.launch {
            try {
                auth.sendPasswordResetEmail(email.toString()).await()
                withContext(Dispatchers.Main) {
                    result.invoke("Check your email to reset your password!")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.invoke(e.message.toString())
                }
            }
        }
    }

    override fun googleSignIn(result: (String) -> Unit) {

    }


}