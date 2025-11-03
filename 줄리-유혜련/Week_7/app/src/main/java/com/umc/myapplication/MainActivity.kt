package com.umc.myapplication

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationBarView
import com.umc.myapplication.databinding.ActivityMainBinding
import com.umc.myapplication.fragment.HomeFragment
import com.umc.myapplication.fragment.LockerFragment
import com.umc.myapplication.fragment.LookFragment
import com.umc.myapplication.fragment.SearchFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val RESULT_ALBUM_TITLE = "result_album_title"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var songResultLauncher: ActivityResultLauncher<Intent>

    // MusicService 바인딩
    private var musicService: MusicService? = null
    private var bound = false
    private var miniSeekJob: Job? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            bound = true
            startMiniSeekbarSync()
            attachMiniSeekbarListener()
            runCatching {
                val title = musicService?.getCurrentTitle().orEmpty()
                val artist = musicService?.getCurrentArtist().orEmpty()
                if (title.isNotBlank() || artist.isNotBlank()) {
                    updateMiniPlayer(title, artist)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            musicService = null
            miniSeekJob?.cancel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
         installSplashScreen()
         Thread.sleep(3000)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val albumTitle = result.data?.getStringExtra(RESULT_ALBUM_TITLE)
                if (!albumTitle.isNullOrBlank()) {
                    Toast.makeText(this, "선택한 앨범: $albumTitle", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 미니플레이어 클릭 → SongActivity 이동
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title",  binding.miniSongTitleTv.text?.toString() ?: "")
                putExtra("singer", binding.miniSongArtistTv.text?.toString() ?: "")
            }
            songResultLauncher.launch(intent)
        }

        initBottomNavigation()
        if (savedInstanceState == null) {
            binding.mainBnv.selectedItemId = R.id.homeFragment
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MusicService::class.java).also {
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        miniSeekJob?.cancel()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener(onNavSelected)
    }

    private val onNavSelected = NavigationBarView.OnItemSelectedListener { item ->
        val fragment = when (item.itemId) {
            R.id.homeFragment   -> HomeFragment()
            R.id.lookFragment   -> LookFragment()
            R.id.searchFragment -> SearchFragment()
            R.id.lockerFragment -> LockerFragment()
            else -> return@OnItemSelectedListener false
        }

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.main_frm, fragment)
            .commit()

        true
    }

    /** 미니플레이어 라벨 업데이트 */
    fun updateMiniPlayer(title: String, artist: String) {
        binding.mainPlayerCl.visibility = View.VISIBLE
        binding.miniSongTitleTv.text = title
        binding.miniSongArtistTv.text = artist
    }

    /** 서비스의 position/duration을 읽어와 미니 SeekBar 동기화 */
    private fun startMiniSeekbarSync() {
        miniSeekJob?.cancel()
        miniSeekJob = lifecycleScope.launch {
            while (bound && musicService != null) {
                val svc = musicService!!
                val dur = svc.getDuration()
                val pos = svc.getCurrentPosition()

                if (dur > 0) {
                    binding.mainMiniplayerProgressSb.max = dur
                    binding.mainMiniplayerProgressSb.progress = pos

                    if (binding.mainPlayerCl.visibility != View.VISIBLE) {
                        binding.mainPlayerCl.visibility = View.VISIBLE
                    }
                }
                delay(100)
            }
        }
    }

    /** 미니 SeekBar 드래그 → 서비스 seekTo로 반영 */
    private fun attachMiniSeekbarListener() {
        // ⚠️ miniSongSeekBar는 네 레이아웃의 실제 ID로
        binding.mainMiniplayerProgressSb.setOnSeekBarChangeListener(
            object : android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    sb: android.widget.SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    if (fromUser) {
                        musicService?.seekTo(progress) // 밀리초 기준
                    }
                }
                override fun onStartTrackingTouch(sb: android.widget.SeekBar?) {}
                override fun onStopTrackingTouch(sb: android.widget.SeekBar?) {}
            }
        )
    }
}
