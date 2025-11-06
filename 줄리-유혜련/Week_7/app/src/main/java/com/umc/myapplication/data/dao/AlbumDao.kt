package com.umc.myapplication.data.dao

import com.umc.myapplication.data.entity.AlbumEntity
import androidx.room.*

@Dao
interface AlbumDao {

    // 앨범 추가 (한 개)
    @Insert
    fun insert(album: AlbumEntity): Long

    // 모든 앨범 조회
    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<AlbumEntity>

    // id로 특정 앨범 조회
    @Query("SELECT * FROM AlbumTable WHERE id = :albumId")
    fun getAlbumById(albumId: Int):AlbumEntity?
}
