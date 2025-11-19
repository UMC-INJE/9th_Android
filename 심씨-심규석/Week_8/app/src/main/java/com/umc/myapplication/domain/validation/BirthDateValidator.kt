package com.umc.myapplication.domain.validation

import java.time.LocalDate

class BirthDateValidator (
    private val fieldName: String = "birthDate"
) : Validator<String> {
    private val eightDigits = Regex("^\\d{8}$")
    override fun validate(input: String): ValidationResult {
        if (input.isBlank()) return ValidationResult.Valid
        if (!eightDigits.matches(input)) {
            return ValidationResult.Invalid(listOf(ValidationError.Field(fieldName, "생년월일 형식을 확인하세요(yyyyMMdd).")))
        }
        val yyyy = input.substring(0, 4).toInt()
        val mm = input.substring(4, 6).toInt()
        val dd = input.substring(6, 8).toInt()
        if (mm !in 1..12) {
            return ValidationResult.Invalid(listOf(ValidationError.InvalidBirthDate))
        }
        return try {
            LocalDate.of(yyyy, mm, dd)
            ValidationResult.Valid
        } catch (e: Exception) {
            ValidationResult.Invalid(listOf(ValidationError.InvalidBirthDate))
        }
    }
}