// KakaoLoginUtil.kt
package com.umc.myapplication.util

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

private const val TAG = "KAKAO_LOGIN"

data class KakaoLoginResult(
    val accessToken: String,
    val id: Long?,
    val nickname: String?,
    val email: String?,
    val profileImageUrl: String?
)

fun kakaoLogin(
    context: Context,
    onSuccess: (KakaoLoginResult) -> Unit,
    onError: (Throwable?) -> Unit
) {
    val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = kakaoCallback@{ token, error ->
        if (error != null) {
            Log.e(TAG, "카카오 로그인 실패", error)
            onError(error)
            return@kakaoCallback
        }

        if (token == null) {
            Log.e(TAG, "카카오 로그인 실패: token is null")
            onError(null)
            return@kakaoCallback
        }

        Log.i(TAG, "카카오 로그인 성공, accessToken=${token.accessToken}")

        // ✅ 로그인 성공 → 사용자 정보 요청
        UserApiClient.instance.me { user, meError ->
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError)
                onError(meError)
                return@me
            }

            if (user == null) {
                Log.e(TAG, "사용자 정보 요청 실패: user is null")
                onError(null)
                return@me
            }

            val result = KakaoLoginResult(
                accessToken = token.accessToken,
                id = user.id,
                nickname = user.kakaoAccount?.profile?.nickname,
                email = user.kakaoAccount?.email,
                profileImageUrl = user.kakaoAccount?.profile?.profileImageUrl
            )

            Log.i(TAG, "Kakao user = $result")
            onSuccess(result)
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoCallback)
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
    }
}
