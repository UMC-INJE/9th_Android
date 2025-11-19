// di/Qualifiers.kt
package com.umc.myapplication.di

import javax.inject.Qualifier

@Qualifier annotation class EmailValidatorQ
@Qualifier annotation class PasswordLengthValidatorQ
@Qualifier annotation class PasswordComplexityValidatorQ
@Qualifier annotation class BirthDateValidatorQ
