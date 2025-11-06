package com.umc.myapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umc.myapplication.data.dao.AlbumDao
import com.umc.myapplication.data.dao.SongDao
import com.umc.myapplication.data.entity.AlbumEntity
import com.umc.myapplication.data.entity.SongEntity

@Database(
    entities = [AlbumEntity::class, SongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FloDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun songDao(): SongDao

    companion object {
        @Volatile private var INSTANCE: FloDatabase? = null

        fun getInstance(context: Context): FloDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FloDatabase::class.java,
                    "flo.db" // DB 파일명
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
