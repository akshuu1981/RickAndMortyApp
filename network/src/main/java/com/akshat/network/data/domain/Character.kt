package com.akshat.network.data.domain

import kotlinx.serialization.Serializable


data class Character(
    val id: Int,
    val name: String,
    val origin: Origin,
    val created: String,
    val episodeUrls : List<String>,
    val gender: CharacterGender,
    val status : CharacterStatus,
    val imageUrl : String,
    val location : Location,
    val species : String,
    val type : String,
    )
{
    data class Origin(
        val name:String)

    data class Location(val name: String, val url: String)
}