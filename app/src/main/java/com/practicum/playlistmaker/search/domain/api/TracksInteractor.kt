package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    operator fun invoke(query: String): Flow<Pair<List<Track>?, String?>>
}