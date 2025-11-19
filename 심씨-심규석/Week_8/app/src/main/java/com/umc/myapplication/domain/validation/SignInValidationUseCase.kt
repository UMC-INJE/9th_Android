package com.umc.myapplication.domain.validation

import javax.inject.Inject

data class SignInParams(val email: String, val password: String)

class SignInValidationUseCase @Inject constructor(
    private val emailValidator: Validator<String>
) {
    fun execute(params: SignInParams): ValidationResult {
        val emailResult = emailValidator.validate(params.email.trim())
        if (emailResult is ValidationResult.Invalid) return emailResult
        if (params.password.isBlank()) {
            return ValidationResult.Invalid(listOf(ValidationError.Field("password", "비밀번호를 입력하세요.")))
        }
        return ValidationResult.Valid
    }
}
