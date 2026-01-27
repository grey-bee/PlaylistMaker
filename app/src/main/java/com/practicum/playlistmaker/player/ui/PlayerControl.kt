package com.practicum.playlistmaker.player.ui

import kotlinx.coroutines.flow.StateFlow

interface PlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun startForeground()
    fun stopForeground()
}