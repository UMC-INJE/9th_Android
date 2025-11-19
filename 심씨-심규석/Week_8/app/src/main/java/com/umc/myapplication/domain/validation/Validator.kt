package com.umc.myapplication.domain.validation

interface Validator<T> {
    fun validate(input : T) : ValidationResult
}