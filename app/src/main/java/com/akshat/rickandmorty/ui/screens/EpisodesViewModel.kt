package com.akshat.rickandmorty.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshat.rickandmorty.model.CharacterRepository
import com.akshat.rickandmorty.model.EpisodeRepository
import com.akshat.rickandmorty.state.EpisodeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EpisodeViewState>(value = EpisodeViewState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchEpisodes(characterId: Int) = viewModelScope.launch {
        _uiState.update { EpisodeViewState.Loading }
        characterRepository.fetchCharacter(characterId).onSuccess { character ->
            viewModelScope.launch {
                episodeRepository.getEpisodes(character.episodeUrls).onFailure { exception ->
                    _uiState.update {
                        EpisodeViewState.Error(
                            message = exception.message ?: "Unknown error"
                        )
                    }
                }.onSuccess { episodes ->
                    _uiState.update { EpisodeViewState.Success(character = character, episodes = episodes) }
                }
            }
        }.onFailure {exception->
            _uiState.update {
                EpisodeViewState.Error(
                    message = exception.message ?: "Unknown error"
                )
            }
        }

    }
}