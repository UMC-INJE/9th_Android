package com.umc.myapplication.domain.validation

import android.util.Patterns
import javax.inject.Inject

class EmailValidator @Inject constructor() : Validator<String> {
    override fun validate(input: String): ValidationResult {
        val ok = input.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(input).matches()
        return if (ok) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(ValidationError.Field("email", "이메일 형식을 확인하세요.")))
    }
}
