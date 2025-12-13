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

    suspend fun getCharacter(id: Int): Character{
        return Client.get("https://rickandmortyapi.com/api/character/$id")
            .body<RemoteCharacter>()
            .toDomainCharacter()
    }
}