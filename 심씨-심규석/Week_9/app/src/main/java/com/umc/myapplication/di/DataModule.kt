package com.umc.myapplication.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.umc.myapplication.data.CategoryRepository
import com.umc.myapplication.data.ProductRepository
import com.umc.myapplication.data.UserLikedRepository
import com.umc.myapplication.data.auth.FirebaseAuthRepository
import com.umc.myapplication.domain.auth.AuthRepository
import com.umc.myapplication.domain.auth.SignInWithEmailUseCase
import com.umc.myapplication.domain.auth.SignUpWithEmailUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    // Realtime Database
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideProductRepository(db: FirebaseDatabase): ProductRepository =
        ProductRepository(db)
    @Provides
    fun provideUserLikedRepository(
        db: FirebaseDatabase,
        auth: AuthRepository
    ): UserLikedRepository = UserLikedRepository(db, auth)
    @Provides
    @Singleton
    fun provideCategoryRepository(db: FirebaseDatabase): CategoryRepository =
        CategoryRepository(db)

    // Firebase Auth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance() // 기본 인스턴스 제공 [web:53]


    @Provides
    fun provideSignUpWithEmailUseCase(repo: AuthRepository): SignUpWithEmailUseCase =
        SignUpWithEmailUseCase(repo)

    @Provides
    fun provideSignInWithEmailUseCase(repo: AuthRepository): SignInWithEmailUseCase =
        SignInWithEmailUseCase(repo)
}
