package com.umc.myapplication.domain.auth

import com.umc.myapplication.data.models.request.SignInRequest
import com.umc.myapplication.data.models.request.SignUpRequest
import com.umc.myapplication.domain.model.User

interface AuthRepository {

    suspend fun signUpWithEmail(form : SignUpRequest): Result<User>
    suspend fun signInWithEmail(form : SignInRequest): Result<User>
    suspend fun signOut(): Result<Unit>

    fun getCurrentUser(): User?
}