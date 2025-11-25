package com.umc.myapplication.data.models

data class UserLikes(
    var uid: String,
    var likedProductIds: List<Int> = emptyList()
)
