package com.akshat.network

import android.util.Log
import com.akshat.network.data.domain.Character
import com.akshat.network.data.domain.Episode
import com.akshat.network.data.remote.RemoteCharacter
import com.akshat.network.data.remote.RemoteEpisode
import com.akshat.network.data.remote.toDomainCharacter
import com.akshat.network.data.remote.toDomainEpisode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    private val Client = HttpClient(Android) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var cache = mutableMapOf<Int, Character>()

    suspend fun getCharacter(id: Int): ApiOperation<Character>{
        cache[id]?.let {
            Log.d("KtorClient", "Returning cached character")
            return ApiOperation.Success(it) }
        return safeApiCall {
            Client.get("https://rickandmortyapi.com/api/character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also { cache[id] = it }

        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>>{
        val episodesList = episodeIds.joinToString(separator = ",")
        Log.d("KtorClient", "Getting episodes $episodesList")
        return safeApiCall {
            Client.get("https://rickandmortyapi.com/api/episode/$episodesList")
                .body<List<RemoteEpisode>>()
                .map {  it.toDomainEpisode() }
//                .also { cache[id] = it }

        }
    }
    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T>{
        return try{
            ApiOperation.Success(data = apiCall())
        }catch (exception: Exception){
            ApiOperation.Failure(exception = exception)
        }
    }
}

sealed interface ApiOperation<T>{
    data class Success<T>(val data: T): ApiOperation<T>
    data class Failure<T>(val exception: Exception): ApiOperation<T>

    fun onSuccess(block: (T) -> Unit): ApiOperation<T>{
        if(this is Success) block(data)
        return this
    }


    fun onFailure(block: (Exception) -> Unit): ApiOperation<T>{
        if(this is Failure) block(exception)
        return this
    }
}