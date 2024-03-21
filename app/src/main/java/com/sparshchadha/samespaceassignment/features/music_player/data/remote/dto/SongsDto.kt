package com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto

data class SongsDto(
    val data: List<SongDetails>
)

data class SongDetails(
    val accent: String,
    val artist: String,
    val cover: String,
    val date_created: String,
    val date_updated: String,
    val id: Int,
    val name: String,
    val sort: Any,
    val status: String,
    val top_track: Boolean,
    val url: String,
    val user_created: String,
    val user_updated: String
)