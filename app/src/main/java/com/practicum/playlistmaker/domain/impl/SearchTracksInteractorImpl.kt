package com.practicum.playlistmaker.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(
    private val repository: TracksRepository,
) :
    SearchTracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun execute(
        query: String,
        consumer: SearchTracksInteractor.Consumer
    ) {
        executor.execute {
            val handler = Handler(Looper.getMainLooper())
            try {
                val result = repository.search(query)
                handler.post {
                    consumer.consume(result)
                }
            } catch (_: Exception) {
                handler.post {
                    consumer.consume(null)
                }
            }
        }
    }

}