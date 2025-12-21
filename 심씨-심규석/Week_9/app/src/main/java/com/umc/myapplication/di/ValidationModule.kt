package com.umc.myapplication.di

import com.umc.myapplication.domain.validation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
// di/ValidationModule.kt
@Module
@InstallIn(ViewModelComponent::class)
object ValidationModule {

    @EmailValidatorQ
    @Provides
    fun provideEmailValidator(): Validator<String> = EmailValidator()

    @PasswordLengthValidatorQ
    @Provides
    fun providePasswordLengthValidator(): Validator<String> = PasswordLengthValidator()

    @PasswordComplexityValidatorQ
    @Provides
    fun providePasswordComplexityValidator(): Validator<String> = PasswordComplexityValidator()

    @BirthDateValidatorQ
    @Provides
    fun provideBirthDateValidator(): Validator<String> = BirthDateValidator()

    @Provides
    fun provideSignInValidationUseCase(
        @EmailValidatorQ emailValidator: Validator<String>
    ): SignInValidationUseCase = SignInValidationUseCase(emailValidator)

    @Provides
    fun provideSignUpValidationUseCase(
        @EmailValidatorQ emailValidator: Validator<String>,
        @PasswordLengthValidatorQ pwLen: Validator<String>,
        @PasswordComplexityValidatorQ pwComplex: Validator<String>,
        @BirthDateValidatorQ birth: Validator<String>
    ): SignUpValidationUseCase = SignUpValidationUseCase(
        emailValidator, pwLen, pwComplex, birth
    )
}
