package com.umc.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private val CHANNEL_ID = "ForegroundMusicService"
    private val NOTI_ID = 713

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()

    // 현재 재생 중 곡 정보 (Main/Song에서 라벨 동기화용)
    private var currentSongTitle: String = "Unknown Title"
    private var currentSongArtist: String = "Unknown Artist"

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val initialTitle = intent?.getStringExtra("songTitle") ?: "Unknown Title"
        val initialArtist = intent?.getStringExtra("songArtist") ?: "Unknown Artist"
        val isPlaying = intent?.getBooleanExtra("isPlaying", false) ?: false

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music_lilac).apply {
                setOnCompletionListener {
                    seekTo(0)
                    pause()
                }
            }
            currentSongTitle = initialTitle
            currentSongArtist = initialArtist
            if (isPlaying) {
                mediaPlayer?.start()
            }
        }

        val notification = createNotification()
        startForeground(NOTI_ID, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder = binder

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Music Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("음악 재생 중")
            .setContentText("$currentSongTitle 이/가 재생 중입니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    fun playMusic() { mediaPlayer?.start() }
    fun pauseMusic() { mediaPlayer?.pause() }

    fun seekTo(position: Int) {
        mediaPlayer?.let { mp ->
            val safe = position.coerceIn(0, (mp.duration).coerceAtLeast(0))
            mp.seekTo(safe)
        }
    }

    fun updateCurrentSongInfo(title: String, artist: String) {
        currentSongTitle = title
        currentSongArtist = artist
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0
    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun getCurrentTitle(): String = currentSongTitle
    fun getCurrentArtist(): String = currentSongArtist

    fun restart() {
        mediaPlayer?.let {
            it.seekTo(0)
            it.start()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
