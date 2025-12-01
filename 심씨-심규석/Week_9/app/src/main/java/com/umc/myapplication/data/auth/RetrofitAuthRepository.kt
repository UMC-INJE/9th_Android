package com.umc.myapplication.data.auth

import com.umc.myapplication.data.models.request.SignInRequest
import com.umc.myapplication.data.models.request.SignUpRequest
import com.umc.myapplication.data.remote.ApiService
import com.umc.myapplication.domain.auth.AuthRepository
import com.umc.myapplication.domain.model.User
import com.umc.myapplication.domain.session.SessionStorage
import javax.inject.Inject

class RetrofitAuthRepository @Inject constructor(
    private val api: ApiService,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signUpWithEmail(form: SignUpRequest): Result<User> {
        return try {
            val response = api.signUp(form)
            val data = response.data
                ?: return Result.failure(Exception("Empty response body"))

            val user = User(
                memberId = data.memberId
            )
            // 회원가입 시 바로 로그인된 상태로 취급할 거면 저장
            sessionStorage.saveUser(user)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(form: SignInRequest): Result<User> {
        return try {
            val response = api.signIn(form)
            val data = response.data
                ?: return Result.failure(Exception("Empty response body"))

            val user = User(
                memberId = data.memberId,
                name = data.name,
                accessToken = data.accessToken
            )

            // 로그인 성공 시 유저 정보/토큰 로컬 저장
            sessionStorage.saveUser(user)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            // 서버에 /logout API가 없다면 로컬 세션만 삭제
            sessionStorage.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        // 앱 시작 시 / 화면 진입 시 현재 로그인 유저 가져올 때 사용
        //refresh-access라면 로그인 로직 넣을 것
        return sessionStorage.getUser()
    }
}