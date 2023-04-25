package com.buyit.buyit.start.interfaces

interface RegisterListener {
    fun onEmptyName()
    fun onEmptyEmail()
    fun onEmptyPassword()
    fun onStarted()
    fun onSuccess()
}