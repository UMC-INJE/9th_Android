package com.umc.myapplication

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun signUp(user: User) {

        val signUpService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signUpService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: AuthResponse = response.body()!!

                    Log.d("SIGNUP-RESPONSE", signUpResponse.toString())

                    when (val code = signUpResponse.code) {
                        "COMMON201" -> {
                            val memberId = signUpResponse.data?.memberId ?: -1
                            signUpView.onSignUpSuccess(memberId)
                        }
                        "AUTH400_1" -> signUpView.onSignUpFailure(signUpResponse.message)
                        else -> signUpView.onSignUpFailure(signUpResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                signUpView.onSignUpFailure(t.message ?: "네트워크 오류가 발생했습니다.")
            }
        })
    }


    fun login(user: User) {
        val loginService = getRetrofit().create(AuthRetrofitInterface::class.java)

        loginService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse: AuthResponse = response.body()!!

                    when (val code = loginResponse.code) {
                        "COMMON200_1" -> loginView.onLoginSuccess(code,loginResponse.data!! )
                        "AUTH404_1" -> loginView.onLoginFailure(loginResponse.message)
                        else -> loginView.onLoginFailure(loginResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                loginView.onLoginFailure(t.message ?: "네트워크 오류가 발생했습니다.")
            }
        })
    }
}