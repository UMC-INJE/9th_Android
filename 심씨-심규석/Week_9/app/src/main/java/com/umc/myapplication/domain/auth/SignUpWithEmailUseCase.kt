package com.umc.myapplication.domain.auth

import com.umc.myapplication.data.models.request.SignUpRequest
import com.umc.myapplication.domain.model.User

class SignUpWithEmailUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(form : SignUpRequest): Result<User> =
        repo.signUpWithEmail(form = form)
}