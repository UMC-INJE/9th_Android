package com.umc.myapplication


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.umc.myapplication.databinding.ActivitySongBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding

    private var currentSong: Song? = null
    private var musicService: MusicService? = null
    private var isBound = false
    private var updateJob: Job? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            updateUI()
            updateSeekbar()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicService = null
            updateJob?.cancel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트로 받은 제목/가수 → currentSong 구성
        val title = intent.getStringExtra("title") ?: "Unknown"
        val singer = intent.getStringExtra("singer") ?: "Unknown"
        currentSong = Song(
            id = 1,
            title = title,
            singer = singer,
            album = "Unknown",
            coverImg = R.drawable.ic_launcher_foreground
        )

        // 상단 텍스트 표시
        binding.songMusicTitleTv.text = currentSong!!.title
        binding.songSingerNameTv.text = currentSong!!.singer

        // UI 리스너
        binding.songDownIb.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(MainActivity.RESULT_ALBUM_TITLE, binding.songMusicTitleTv.text.toString())
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        //재생&멈춤 버튼 터치 시 MediaPlayer에 반영
        binding.songMiniplayerIv.setOnClickListener {
            ContextCompat.startForegroundService(this, Intent(this, MusicService::class.java).apply {
                putExtra("songTitle", currentSong!!.title)
                putExtra("songArtist", currentSong!!.singer)
                putExtra("isPlaying", true)
            })

            musicService?.playMusic()
            checkPlay()
        }
        binding.songPauseIv.setOnClickListener {
            musicService?.pauseMusic()
            checkPlay()
        }
        // 이전/다음 둘 다 곡을 처음부터
        binding.songPreviousIv.setOnClickListener {
            musicService?.restart()
            binding.songProgressSb.progress = 0
            binding.songStartTimeTv.text = "00:00"
        }
        binding.songNextIv.setOnClickListener {
            musicService?.restart()
            binding.songProgressSb.progress = 0
            binding.songStartTimeTv.text = "00:00"
        }

        //SeekBar 터치 시 MediaPlayer에 반영
        binding.songProgressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService?.seekTo(progress)
                    binding.songStartTimeTv.text = millisToTime(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, MusicService::class.java), connection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        updateJob?.cancel()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    private fun updateUI() {
        // 곡 정보 싱크
        binding.songMusicTitleTv.text = currentSong?.title ?: "Unknown"
        binding.songSingerNameTv.text = currentSong?.singer ?: "Unknown"

        // SeekBar 최대값/시간 텍스트
        val duration = musicService?.getDuration() ?: 0
        binding.songProgressSb.max = duration
        binding.songEndTimeTv.text = millisToTime(duration)
        binding.songStartTimeTv.text = millisToTime(musicService?.getCurrentPosition() ?: 0)

        // 재생/일시정지 아이콘 상태
        checkPlay()
    }

    // 재생 상태에 따라 아이콘 토글
    private fun checkPlay() {
        val playing = musicService?.isPlaying() == true
        if (playing) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    private fun updateSeekbar() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch(Dispatchers.Main) {
            while (isBound && musicService != null) {
                val pos = musicService!!.getCurrentPosition()
                val dur = musicService!!.getDuration()
                binding.songProgressSb.max = dur
                binding.songProgressSb.progress = pos
                binding.songStartTimeTv.text = millisToTime(pos)
                delay(100)
            }
        }
    }

    private fun millisToTime(ms: Int): String {
        val totalSec = ms / 1000
        val m = totalSec / 60
        val s = totalSec % 60
        return String.format("%02d:%02d", m, s)
    }
}
