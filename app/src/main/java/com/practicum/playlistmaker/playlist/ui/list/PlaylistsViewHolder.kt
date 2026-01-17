package com.practicum.playlistmaker.playlist.ui.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.playlist.domain.model.Playlist

class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistImage: ImageView = itemView.findViewById(R.id.image)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val trackQty: TextView = itemView.findViewById(R.id.trackQty)

    fun bind(model: Playlist) {
        Glide.with(itemView)
            .load(model.imagePath)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(2.dpToPx(itemView.context)))
            .into(playlistImage)
        playlistName.text = model.name
        trackQty.text = "${model.trackCount} треков" //TODO убрать треков в стринги
    }
}