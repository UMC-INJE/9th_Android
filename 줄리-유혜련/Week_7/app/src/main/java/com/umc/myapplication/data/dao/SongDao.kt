package com.umc.myapplication.data.dao

import com.umc.myapplication.data.entity.SongEntity
import androidx.room.*

@Dao
interface SongDao {

    // 노래 추가 (한 개)
    @Insert
    fun insert(song: SongEntity): Long

    // 모든 노래 조회
    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<SongEntity>

    // 특정 앨범에 속한 노래들 조회
    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumId")
    fun getSongsByAlbum(albumId: Int): List<SongEntity>
}
