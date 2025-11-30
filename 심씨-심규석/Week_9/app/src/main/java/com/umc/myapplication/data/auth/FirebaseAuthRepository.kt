package com.umc.myapplication.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.umc.myapplication.data.models.request.SignInRequest
import com.umc.myapplication.data.models.request.SignUpRequest
import com.umc.myapplication.domain.auth.AuthRepository
import com.umc.myapplication.domain.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signUpWithEmail(form: SignUpRequest): Result<User> =
        suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(form.email, form.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val u = auth.currentUser
                        cont.resume(Result.success(User(
                            accessToken = u?.uid.orEmpty(),
                        )))
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("SignUp failed")))
                    }
                }
        }

    override suspend fun signInWithEmail(
        form: SignInRequest
    ): Result<User> = suspendCancellableCoroutine { cont ->
        auth.signInWithEmailAndPassword(form.email, form.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val u = auth.currentUser
                    cont.resume(Result.success(User(accessToken = u?.uid.orEmpty())))
                } else {
                    cont.resume(Result.failure(task.exception ?: Exception("SignIn failed")))
                }
            }
    }
    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut() // 현재 사용자 세션 해제
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User {
        val auth = auth.currentUser
        return User(
            accessToken = auth?.uid
        )
    }
}