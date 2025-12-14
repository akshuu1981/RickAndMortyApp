package com.akshat.network

import com.akshat.network.data.domain.Character
import com.akshat.network.data.remote.RemoteCharacter
import com.akshat.network.data.remote.toDomainCharacter
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

    suspend fun getCharacter(id: Int): ApiOperation<Character>{
        return safeApiCall {
            Client.get("https://rickandmortyapi.com/api/character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()

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