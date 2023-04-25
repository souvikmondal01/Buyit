package com.buyit.buyit.start.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import com.buyit.buyit.start.interfaces.LoginListener
import com.buyit.buyit.start.interfaces.RegisterListener
import com.buyit.buyit.start.models.User
import com.buyit.buyit.start.repositories.StartRepository

class StartViewModel(private val repository: StartRepository) : ViewModel() {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var registerListener: RegisterListener? = null
    var loginListener: LoginListener? = null
    private val time = System.currentTimeMillis().toString()

    fun onRegisterClick(view: View) {
        registerListener?.onStarted()
        if (name.isNullOrEmpty()) {
            registerListener?.onEmptyName()
            return
        }
        if (email.isNullOrEmpty()) {
            registerListener?.onEmptyEmail()
            return
        }
        if (password.isNullOrEmpty()) {
            registerListener?.onEmptyPassword()
            return
        }
        registerListener?.onSuccess()
    }

    fun registerUser(result: (String) -> Unit) {
        repository.registerUser(User(null, name, email, password, time)) {
            result.invoke(it)
        }
    }

    fun onLoginClick(view: View) {
        loginListener?.onStarted()
        if (email.isNullOrEmpty()) {
            loginListener?.onEmptyEmail()
            return
        }
        if (password.isNullOrEmpty()) {
            loginListener?.onEmptyPassword()
            return
        }
        loginListener?.onSuccess()
    }

    fun loginUser(result: (String) -> Unit) {
        repository.loginUser(User(null, name, email, password)) {
            result.invoke(it)
        }
    }

    fun onForgotPassword(view: View) {
        loginListener?.onStarted()
        if (email.isNullOrEmpty()) {
            loginListener?.onEmptyEmail()
            return
        }
        loginListener?.restPassword()
    }

    fun resetPassword(result: (String) -> Unit) {
        repository.resetPassword(email.toString()) {
            result.invoke(it)
        }
    }

    fun onGoogleClick(view: View) {
        loginListener?.googleLogin()
    }



}