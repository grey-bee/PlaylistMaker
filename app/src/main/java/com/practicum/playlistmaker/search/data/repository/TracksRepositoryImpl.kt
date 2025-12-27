package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.toTrack
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.model.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun search(query: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        return if (response.resultCode == 200) {
            (response as TrackSearchResponse).results.map { it.toTrack() }
        } else {
            emptyList()
        }
    }
}