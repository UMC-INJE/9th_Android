package com.umc.myapplication.domain.validation

sealed class ValidationError {
    data class Field(val field: String, val message: String) : ValidationError()
    object InvalidEmail : ValidationError()
    object PasswordTooShort : ValidationError()
    object PasswordNotComplex : ValidationError()
    object InvalidBirthDate : ValidationError()
}