package com.practicum.playlistmaker.playlist.domian.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val trackIds: List<String>,
    val trackCount: Int
)

