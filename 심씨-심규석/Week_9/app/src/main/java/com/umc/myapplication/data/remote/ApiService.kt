package com.umc.myapplication.data.remote

import com.umc.myapplication.data.models.request.SignInRequest
import com.umc.myapplication.data.models.request.SignUpRequest
import com.umc.myapplication.data.models.request.UpdateUserRequest
import com.umc.myapplication.data.models.response.BaseResponse
import com.umc.myapplication.data.models.response.SignInResponseData
import com.umc.myapplication.data.models.response.SignUpResponseData
import com.umc.myapplication.data.models.response.UpdateUserResponseData
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    @PATCH("/users")
    suspend fun updateUser(
        @Header("Authorization") authorization: String,
        @Body body: UpdateUserRequest
    ): BaseResponse<UpdateUserResponseData>
}