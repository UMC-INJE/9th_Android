package com.umc.myapplication.domain.validation

import com.umc.myapplication.domain.model.SignUpForm

class SignUpValidationUseCase(
    private val emailValidator: Validator<String> = EmailValidator(),
    private val passwordLengthValidator: Validator<String> = PasswordLengthValidator(),
    private val passwordComplexityValidator: Validator<String> = PasswordComplexityValidator(),
    private val birthDateValidator: Validator<String> = BirthDateValidator()
) {
    fun execute(form: SignUpForm): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        when (val r = emailValidator.validate(form.email.trim())) {
            is ValidationResult.Invalid -> errors += r.errors
            else -> Unit
        }
        when (val r = passwordLengthValidator.validate(form.password)) {
            is ValidationResult.Invalid -> errors += r.errors
            else -> Unit
        }
        when (val r = passwordComplexityValidator.validate(form.password)) {
            is ValidationResult.Invalid -> errors += r.errors
            else -> Unit
        }
        when (val r = birthDateValidator.validate(form.birthDate)) {
            is ValidationResult.Invalid -> errors += r.errors
            else -> Unit
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }
}
