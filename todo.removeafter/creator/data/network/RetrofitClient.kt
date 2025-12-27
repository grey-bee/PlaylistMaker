package com.practicum.playlistmaker.old.creator.data.network

import com.practicum.playlistmaker.old.creator.data.NetworkClient
import com.practicum.playlistmaker.old.creator.data.dto.Response
import com.practicum.playlistmaker.old.creator.data.dto.TrackSearchRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://itunes.apple.com"
class RetrofitClient : NetworkClient {


    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val appleMusicService = retrofit.create(ApiService::class.java)

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