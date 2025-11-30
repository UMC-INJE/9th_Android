package com.umc.myapplication.data.session

import android.content.SharedPreferences
import com.umc.myapplication.domain.model.User
import com.umc.myapplication.domain.session.SessionStorage
import javax.inject.Inject
import kotlin.apply
import kotlin.collections.remove

class PrefsSessionStorage @Inject constructor(
    private val prefs : SharedPreferences
    ) : SessionStorage {
    override fun saveUser(user: User) {
        prefs.edit()
            .putLong("memberId", user.memberId ?: -1L)
            .putString("name", user.name)
            .putString("accessToken", user.accessToken)
            .apply()
    }

    override fun clearUser() {
        prefs.edit()
            .remove("memberId")
            .remove("name")
            .remove("accessToken")
            .apply()
    }

    override fun getUser(): User? {
        val token = prefs.getString("accessToken", null) ?: return null
        val memberId = prefs.getLong("memberId", -1L).takeIf { it != -1L }
        val name = prefs.getString("name", null)

        return User(
            memberId = memberId,
            name = name,
            accessToken = token
        )
    }

}