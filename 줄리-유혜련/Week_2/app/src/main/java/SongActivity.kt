package com.umc.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.myapplication.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}