package com.sparshchadha.samespaceassignment.features.music_player.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.samespaceassignment.Resource
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongsDto
import com.sparshchadha.samespaceassignment.features.music_player.domain.repository.MusicPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel() {
    private val _allSongs = MutableStateFlow<Resource<SongsDto>?>(null)
    val allSongs = _allSongs.asStateFlow()

    init {
        getAllSongs()
    }

    fun getAllSongs() {
        _allSongs.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            musicPlayerRepository.getAllSongs().collect {
                _allSongs.value = it
            }
        }
    }
}