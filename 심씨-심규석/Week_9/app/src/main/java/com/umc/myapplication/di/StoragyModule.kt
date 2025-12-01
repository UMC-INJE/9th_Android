package com.umc.myapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.umc.myapplication.data.CartRepository
import com.umc.myapplication.data.session.PrefsSessionStorage
import com.umc.myapplication.domain.session.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {
    @Provides
    @Singleton
    fun provideCartRepository(
        prefs: SharedPreferences
    ): CartRepository = CartRepository(prefs)
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("com.umc.myapplication.cart.prefs", Context.MODE_PRIVATE)
    @Provides
    @Singleton
    fun provideSessionStorage(
        prefs: SharedPreferences
    ): SessionStorage {
        return PrefsSessionStorage(prefs)
    }


}