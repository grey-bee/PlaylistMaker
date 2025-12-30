package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track

class TrackAdapter(private val tracks: List<Track>, private val onItemClick: (Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_search_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        val item = tracks[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}