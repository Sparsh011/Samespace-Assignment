package com.sparshchadha.samespaceassignment.features.music_player.data.remote.api

import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SongsApi {
    @GET("/items/songs")
    suspend fun getAllSongs(): Response<SongsDto>

    companion object {
        val BASE_URL = "https://cms.samespace.com"
    }
}