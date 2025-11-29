package com.umc.myapplication.data.models.response

data class SignInResponseData(
    val name: String,
    val memberId: Long,
    val accessToken: String
)
