package com.umc.myapplication.data.models.response

data class BaseResponse<T>(
    val status : Boolean,
    val code: String,
    val message: String,
    val data: T?,
)
