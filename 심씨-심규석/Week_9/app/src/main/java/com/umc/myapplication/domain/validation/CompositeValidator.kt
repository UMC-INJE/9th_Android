package com.umc.myapplication.domain.validation

class CompositeValidator<T>(
    private val validators: List<Validator<T>>,
    private val shortCircuit: Boolean = true
) : Validator<T> {
    override fun validate(input: T): ValidationResult {
        val allErrors = mutableListOf<ValidationError>()
        for (v in validators) {
            when (val r = v.validate(input)) {
                is ValidationResult.Valid -> Unit
                is ValidationResult.Invalid -> {
                    allErrors += r.errors
                    if (shortCircuit) return ValidationResult.Invalid(allErrors.toList())
                }
            }
        }
        return if (allErrors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(allErrors.toList())
    }
}
