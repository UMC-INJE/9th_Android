package com.umc.myapplication.domain.auth

import com.umc.myapplication.data.models.request.SignInRequest
import com.umc.myapplication.domain.model.User

class SignInWithEmailUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(form: SignInRequest): Result<User> =
        repo.signInWithEmail(form)
}