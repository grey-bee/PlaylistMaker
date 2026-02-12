package com.practicum.playlistmaker.ui.playlist

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.practicum.playlistmaker.playlist.ui.details.PlaylistState
import com.practicum.playlistmaker.ui.mock.PreviewData

class PlaylistStateProvider: PreviewParameterProvider<PlaylistState> {
    override val values = sequenceOf(
        PlaylistState.Empty,
        PlaylistState.Content(
            playlist = PreviewData.playlist,
            playlistTimeSec = 300,
            playlistTracks = PreviewData.trackList10
        )
    )
}