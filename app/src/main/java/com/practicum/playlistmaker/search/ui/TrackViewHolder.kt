package com.practicum.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.toTimeString

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackInfo: TextView = itemView.findViewById(R.id.track_info)

    @SuppressLint("SetTextI18n")
    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(2.dpToPx(itemView.context)))
            .into(trackImage)
        trackName.text = model.trackName
        trackInfo.text = "${model.artistName} • ${model.trackTimeMillis.toTimeString()}"
    }
}
