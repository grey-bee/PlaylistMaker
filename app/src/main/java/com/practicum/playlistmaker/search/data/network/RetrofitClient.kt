package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitClient(apiService: ApiService) : NetworkClient {
    val appleMusicService: ApiService = apiService

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            withContext(Dispatchers.IO) {
                try {
                    val response = appleMusicService.search(dto.query)
                    response.apply { resultCode = 200 }
                } catch (_: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}