package com.umc.myapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.umc.myapplication.data.CategoryRepository
import com.umc.myapplication.data.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideProductRepository(db: FirebaseDatabase): ProductRepository =
        ProductRepository(db)

    @Provides
    @Singleton
    fun provideCategoryRepository(db: FirebaseDatabase): CategoryRepository =
        CategoryRepository(db)


}