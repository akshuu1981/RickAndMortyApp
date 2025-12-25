package com.akshat.rickandmorty.model

import com.akshat.network.ApiOperation
import com.akshat.network.KtorClient
import com.akshat.network.data.domain.Episode
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun getEpisodes(episodeUrls: List<Int>): ApiOperation<List<Episode>>{
        return ktorClient.getEpisodes(episodeUrls)
    }
}