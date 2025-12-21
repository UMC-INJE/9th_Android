package com.umc.myapplication.di

import com.umc.myapplication.data.auth.FirebaseAuthRepository
import com.umc.myapplication.data.auth.RetrofitAuthRepository
import com.umc.myapplication.domain.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: FirebaseAuthRepository
    ): AuthRepository
}