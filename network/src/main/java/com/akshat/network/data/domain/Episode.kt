package com.akshat.network.data.domain

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val charactersIdInEpisode: List<Int>
)
