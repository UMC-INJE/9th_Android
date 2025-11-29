package com.umc.myapplication.data.remote

import com.umc.myapplication.data.models.request.SignInRequest
import com.umc.myapplication.data.models.request.SignUpRequest
import com.umc.myapplication.data.models.response.BaseResponse
import com.umc.myapplication.data.models.response.SignInResponseData
import com.umc.myapplication.data.models.response.SignUpResponseData
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/signup")
    suspend fun signUp(
        @Body body: SignUpRequest
    ): BaseResponse<SignUpResponseData>

    @POST("/login")
    suspend fun signIn(
        @Body body: SignInRequest
    ): BaseResponse<SignInResponseData>
}