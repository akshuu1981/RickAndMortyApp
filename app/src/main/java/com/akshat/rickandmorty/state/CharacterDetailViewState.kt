package com.akshat.rickandmorty.state

import com.akshat.network.data.domain.Character
import com.akshat.rickandmorty.data.DataPoint

sealed interface CharacterDetailViewState {
    object Loading: CharacterDetailViewState
    data class Success(val character: Character,
        val datapoints : List<DataPoint>): CharacterDetailViewState
    data class Error(val message: String) : CharacterDetailViewState
}