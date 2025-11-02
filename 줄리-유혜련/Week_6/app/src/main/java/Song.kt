package com.umc.myapplication

data class Song(
    val title : String = "",
    val singer : String = "",
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int = 0,
    val album: String = "",
    val id: Int = 0,
    var second: Int = 0
)
