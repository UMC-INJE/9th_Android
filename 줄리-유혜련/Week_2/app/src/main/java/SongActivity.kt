package com.umc.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.myapplication.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(MainActivity.RESULT_ALBUM_TITLE, binding.songMusicTitleTv.text.toString())
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}