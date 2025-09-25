package com.umc.myapplication.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.umc.myapplication.databinding.ActivitySongBinding
import com.umc.myapplication.utils.makeToast

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding

    // 전달받은 값을 보관할 변수
    private var title: String = ""
    private var artist: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) MainActivity에서 넘겨준 데이터 추출
        title = intent.getStringExtra("title").orEmpty()
        artist = intent.getStringExtra("artist").orEmpty()

        makeToast(context = this, message = "$title, $artist")
        // 2) UI에 반영 (필요 시)
        binding.title.text = title
        binding.artist.text = artist

        // 3) 뒤로가기(상단 화살표) 클릭 시 결과 반환
        binding.backArrow.setOnClickListener {
            val data = Intent().apply {
                putExtra(MainActivity.RECIEVED_MAIN_FROM_SONG_ACTIVITY, "OK")
                // 필요하면 편집된 값도 반환 가능
                putExtra("title", title)
                putExtra("artist", artist)
            }
            setResult(RESULT_OK, data)
            finish()
        }

        // 시스템 인셋 적용
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }
    }


}
