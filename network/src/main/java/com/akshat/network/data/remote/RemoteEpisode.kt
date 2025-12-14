package com.akshat.network.data.remote

import com.akshat.network.data.domain.Episode
import kotlinx.serialization.Serializable

@Serializable
data class RemoteEpisode(
    val id : Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>
)

fun RemoteEpisode.toDomainEpisode(): Episode {
    return Episode(
        id = id,
        name = name,
        airDate = air_date,
        seasonNumber = episode.filter { it.isDigit() }.take(2).toInt(),
        episodeNumber = episode.filter {  it.isDigit()}.takeLast(2).toInt(),
        charactersIdInEpisode = characters.map {
            it.split("/").last().toInt()
        })
}
