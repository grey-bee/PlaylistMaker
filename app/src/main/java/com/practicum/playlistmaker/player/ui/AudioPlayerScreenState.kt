package com.practicum.playlistmaker.player.ui

data class AudioPlayerScreenState (
    val playerState: AudioPlayerViewModel.PlayerState,
    val progressTime: String
)