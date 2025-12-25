package com.akshat.rickandmorty.model

import com.akshat.network.ApiOperation
import com.akshat.network.KtorClient
import com.akshat.network.data.domain.Character
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character>{
        return ktorClient.getCharacter(characterId)
    }
}