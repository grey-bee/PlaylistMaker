package com.practicum.playlistmaker.playlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val trackIds: List<String>,
    val trackCount: Int
) : Parcelable

