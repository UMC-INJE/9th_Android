package com.umc.myapplication.model

import android.util.Log

data class AuthScreenUiConfig (
    val title: String,
    val buttonText: String,
    val onClick: (android.view.View) -> Unit
)

val defaultAuthScreenUiConfig = AuthScreenUiConfig(
    title = "기본 제목",
    buttonText = "기본 버튼",
    onClick = { Log.d("AuthScreenUiConfig", "test: 기본 클릭함수 실행")}
)
