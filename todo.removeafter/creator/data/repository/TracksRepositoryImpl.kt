package com.practicum.playlistmaker.old.creator.data.repository

import com.practicum.playlistmaker.old.creator.data.NetworkClient
import com.practicum.playlistmaker.old.creator.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.old.creator.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.old.creator.data.toTrack
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

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