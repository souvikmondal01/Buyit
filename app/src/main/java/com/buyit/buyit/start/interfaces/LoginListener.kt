package com.buyit.buyit.start.interfaces

interface LoginListener {
    fun onEmptyEmail()
    fun onEmptyPassword()
    fun onStarted()
    fun onSuccess()
    fun restPassword()
    fun googleLogin()
}