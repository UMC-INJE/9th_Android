package com.umc.myapplication.data.models.request

data class UpdateUserRequest(
    val memberId: Long,
    val newemail: String,
    val newpassword: String
)
