package com.practicum.playlistmaker.old.creator.data

import com.practicum.playlistmaker.old.creator.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}