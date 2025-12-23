package com.umc.myapplication

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AuthData?

)

data class AuthData(
    @SerializedName("name") val name: String?,
    @SerializedName("memberId") val memberId: Int?,
    @SerializedName("accessToken") val accessToken: String?
)