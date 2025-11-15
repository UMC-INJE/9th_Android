package com.umc.myapplication.domain.model

data class SignUpForm(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = ""
)
