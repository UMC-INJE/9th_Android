package com.umc.myapplication.domain.validation

class PasswordComplexityValidator(
    private val fieldName: String = "password"
) : Validator<String> {
    private val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")
    override fun validate(input: String): ValidationResult {
        val ok = regex.matches(input)
        return if (ok) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(ValidationError.Field(fieldName, "영문 대/소문자와 숫자를 포함해야 합니다.")))
    }
}
