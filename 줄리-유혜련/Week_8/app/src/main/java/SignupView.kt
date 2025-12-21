package com.umc.myapplication

interface SignUpView {
    fun onSignUpSuccess(memberId: Int)
    fun onSignUpFailure(message: String)
}