package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitClient(apiService: ApiService) : NetworkClient {
    val appleMusicService: ApiService = apiService

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val response = appleMusicService.search(dto.query).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}