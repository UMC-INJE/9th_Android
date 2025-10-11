package com.umc.myapplication

data class Song(
    val id: Int,
    val title : String = "",
    val singer : String = "",
    val album: String = "",
    var coverImg: Int
)
