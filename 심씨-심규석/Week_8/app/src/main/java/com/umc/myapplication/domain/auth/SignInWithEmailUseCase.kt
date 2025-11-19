package com.umc.myapplication.domain.auth

import com.umc.myapplication.domain.model.User

class SignInWithEmailUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        repo.signUpWithEmail(email.trim(), password)
}