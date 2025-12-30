package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {
    private val stateLiveData =
        MutableLiveData(AudioPlayerScreenState(PlayerState.DEFAULT, "00:00"))

    fun observePlayerScreenState(): LiveData<AudioPlayerScreenState> =
        stateLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (stateLiveData.value?.playerState == PlayerState.PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateLiveData.postValue(
                AudioPlayerScreenState(
                    PlayerState.PREPARED,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                )
            )
        }
        mediaPlayer.setOnCompletionListener {
            resetTimer()
            stateLiveData.value = (stateLiveData.value?.copy(playerState = PlayerState.PREPARED))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        val newState = stateLiveData.value?.copy(playerState = PlayerState.PLAYING)
        stateLiveData.value = newState
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        val newState = stateLiveData.value?.copy(playerState = PlayerState.PAUSED)
        stateLiveData.value = newState
        handler.removeCallbacksAndMessages(null)
    }

    fun playbackControl() {
        when (stateLiveData.value?.playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PREPARED, PlayerState.PAUSED -> startPlayer()
            else -> {}
        }
    }

    fun stopPlayer() {
        resetTimer()
        mediaPlayer.stop()
        handler.removeCallbacksAndMessages(null)
    }

    private fun startTimerUpdate() {
        val newState = stateLiveData.value?.copy(
            progressTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                mediaPlayer.currentPosition
            )
        )
        stateLiveData.value = newState
        handler.postDelayed(timerRunnable, 200)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        val newState = stateLiveData.value?.copy(
            progressTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
        )
        stateLiveData.postValue(newState)
    }

    enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }
}