package com.umc.myapplication.domain.validation

class PasswordLengthValidator(
    private val min: Int = 8,
    private val fieldName: String = "password"
) : Validator<String> {
    override fun validate(input: String): ValidationResult {
        val ok = input.length >= min
        return if (ok) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(ValidationError.Field(fieldName, "비밀번호는 ${min}자 이상이어야 합니다.")))
    }
}
