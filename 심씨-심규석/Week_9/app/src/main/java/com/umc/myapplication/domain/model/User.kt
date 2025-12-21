package com.umc.myapplication.domain.model

data class User(
    val memberId: Long? = null,      // 서버에서 내려주는 회원 PK (없을 수 있으니 nullable)
    val name: String? = null,        // 서로그인 응답의 name
    val accessToken: String? = null,
    val profileUrl : String?= null,
    //카톡용
    val id : String?= null,
    val email : String ?= null,
)