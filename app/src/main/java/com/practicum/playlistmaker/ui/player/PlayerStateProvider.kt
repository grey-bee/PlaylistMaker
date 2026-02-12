package com.practicum.playlistmaker.ui.player

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.practicum.playlistmaker.player.ui.PlayerState
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.ui.mock.PreviewData

class PlayerStateProvider : PreviewParameterProvider<PlayerPreviewData> {
    override val values = sequenceOf(
        PlayerPreviewData(
            playerState = PlayerState.Playing("00:43"),
            playlistsState = PlaylistsState.Content(PreviewData.playlistList10),
            track = PreviewData.track
        ),
        PlayerPreviewData(
            playerState = PlayerState.Prepared(),
            playlistsState = PlaylistsState.Empty,
            track = PreviewData.track
        )
    )
}