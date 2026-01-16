package com.practicum.playlistmaker.playlist.domian.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val trackIds: String,
    val trackCount: Int
)

