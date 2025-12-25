package com.akshat.rickandmorty.state

import com.akshat.network.data.domain.Character
import com.akshat.network.data.domain.Episode

interface EpisodeViewState {
    object Loading : EpisodeViewState
    data class Success(val character: Character, val episodes: List<Episode>): EpisodeViewState
    data class Error (val message: String): EpisodeViewState
}