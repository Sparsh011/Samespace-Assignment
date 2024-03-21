package com.sparshchadha.samespaceassignment.features.music_player.domain.repository

import com.sparshchadha.samespaceassignment.Resource
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongsDto
import kotlinx.coroutines.flow.Flow

interface MusicPlayerRepository {
    suspend fun getAllSongs(): Flow<Resource<SongsDto>>
}