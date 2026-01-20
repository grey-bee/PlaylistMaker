package com.practicum.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.model.Playlist

class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistCover: ImageView = itemView.findViewById(R.id.playlistCover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val tracksQTY: TextView = itemView.findViewById(R.id.tracksQTY)

    fun bind(model: Playlist) {
        playlistCover.setImageURI(model.imagePath?.toUri())
        playlistName.text = model.name
        tracksQTY.text = itemView.resources.getQuantityString(
            R.plurals.tracks_count,
            model.trackCount,
            model.trackCount
        )
    }
}