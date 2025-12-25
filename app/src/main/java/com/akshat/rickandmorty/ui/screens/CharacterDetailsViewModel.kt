package com.akshat.rickandmorty.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshat.rickandmorty.data.DataPoint
import com.akshat.rickandmorty.model.CharacterRepository
import com.akshat.rickandmorty.state.CharacterDetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {


    private val _internalFlow = MutableStateFlow<CharacterDetailViewState>(
        value = CharacterDetailViewState.Loading)
    val stateFlow = _internalFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        _internalFlow.update { CharacterDetailViewState.Loading }
        characterRepository.fetchCharacter(characterId = characterId)
            .onSuccess { character ->
                val datapoints = buildList {

                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.gender))
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode count", character.episodeUrls.size.toString()))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                }
                _internalFlow.update {
                    CharacterDetailViewState.Success(
                        character = character,
                        datapoints = datapoints
                    )
                }
            }
            .onFailure { exception ->
                _internalFlow.update {
                    CharacterDetailViewState.Error(message = exception.message ?: "Unknown error")
                }
            }
    }
}