package com.umc.myapplication
data class Album(
    val title: String,
    val singer: String,
    val coverImg: Int,
    val songs: List<Song> = emptyList()
)