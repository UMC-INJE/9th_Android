package com.umc.myapplication.domain.auth

import com.umc.myapplication.domain.model.User

interface AuthRepository {

    suspend fun signUpWithEmail(email: String, password: String): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
}