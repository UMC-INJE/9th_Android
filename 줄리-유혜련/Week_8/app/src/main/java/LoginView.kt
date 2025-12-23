package com.umc.myapplication

interface LoginView {
    fun onLoginSuccess(code : String, data: AuthData)
    fun onLoginFailure(message: String)
}