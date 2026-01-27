package com.practicum.playlistmaker.player.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerService() : Service(), PlayerControl {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val binder = PlayerServiceBinder()
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerState = _playerState
    private var previewUrl = ""
    private var trackName = ""
    private var artistName = ""
    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null
    private fun startTimer() {
        timerJob = scope.launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(300L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    private fun createNotificationChannel() {
        // Создание каналов доступно только с Android 8.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            /* id= */ NOTIFICATION_CHANNEL_ID,
            /* name= */ "Music service",
            /* importance= */ NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        // Регистрируем канал уведомлений
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    //region Методы управления Media Player
    private fun initMediaPlayer() {
        if (previewUrl.isEmpty()) return

        mediaPlayer?.setDataSource(previewUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.value = PlayerState.Prepared()
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun startForeground() {
        createNotificationChannel()
        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ SERVICE_NOTIFICATION_ID,
            /* notification = */ createServiceNotification(),
            /* foregroundServiceType = */ getForegroundServiceTypeConstant()
        )
    }

    override fun stopForeground() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer?.stop()
        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayer?.currentPosition) ?: "00:00"
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun createServiceNotification(): Notification {
            return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("$artistName - $trackName")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build()
    }

    // Binder
    override fun onBind(p0: Intent?): IBinder {
        previewUrl = p0?.getStringExtra("preview_url") ?: ""
        trackName = p0?.getStringExtra("track_name") ?: ""
        artistName = p0?.getStringExtra("artist_name") ?: ""
        initMediaPlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        scope.cancel()
        releasePlayer()
        return super.onUnbind(intent)
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 112
    }
}