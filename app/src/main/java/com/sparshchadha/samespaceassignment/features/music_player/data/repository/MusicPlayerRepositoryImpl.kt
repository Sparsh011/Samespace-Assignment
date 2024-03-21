package com.sparshchadha.samespaceassignment.features.music_player.data.repository

import com.sparshchadha.samespaceassignment.Resource
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.api.SongsApi
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongsDto
import com.sparshchadha.samespaceassignment.features.music_player.domain.repository.MusicPlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MusicPlayerRepositoryImpl(
    private val songsApi: SongsApi
): MusicPlayerRepository {
    override suspend fun getAllSongs(): Flow<Resource<SongsDto>> = flow {
        emit(Resource.Loading())
        try {
            val remoteSongs = songsApi.getAllSongs()
            if (remoteSongs.isSuccessful) {
                emit(Resource.Success(data = remoteSongs.body()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(error = e))
        }
    }
}