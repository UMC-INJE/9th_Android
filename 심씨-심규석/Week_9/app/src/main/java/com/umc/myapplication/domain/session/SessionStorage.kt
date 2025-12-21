package com.umc.myapplication.domain.session

import com.umc.myapplication.domain.model.User

interface SessionStorage {
    fun saveUser(user: User)
    fun clearUser()
    fun getUser(): User?
}