package com.practicum.playlist_maker.search.domain.impl

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.utils.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
                is Resource.InternetError -> consumer.consume(null, resource.message)
            }
        }

    }

    override fun getTracksFromSearchHistory(): ArrayList<Track> {
        return repository.getTracksFromSearchHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        repository.addTrackToSearchHistory(track)
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
}