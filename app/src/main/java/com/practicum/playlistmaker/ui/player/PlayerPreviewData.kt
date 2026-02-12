package com.practicum.playlistmaker.ui.player

import com.practicum.playlistmaker.player.ui.PlayerState
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track

data class PlayerPreviewData (
    val playerState: PlayerState,
    val playlistsState : PlaylistsState,
    val track: Track
)