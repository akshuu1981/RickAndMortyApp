package com.akshat.network.data.remote

import com.akshat.network.data.domain.Character
import com.akshat.network.data.domain.CharacterGender
import com.akshat.network.data.domain.CharacterStatus
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val id: Int,
    val name: String,
    val origin: Origin,
    val created: String,
    val episode : List<String>,
    val gender: String,
    val status : String,
    val image : String,
    val location : Location,
    val species : String,
    val type : String,
    )
{
    @Serializable
    data class Origin(
        val name:String)

    @Serializable
    data class Location(val name: String, val url: String)
}

/**
 * This function converts the remote object to the
 * domain/entity class that is used across the app.
 */
fun RemoteCharacter.toDomainCharacter(): Character {

    val characterGender : CharacterGender = when (gender.lowercase()){
        "male" -> CharacterGender.Male
        "female" -> CharacterGender.Female
        "genderless" -> CharacterGender.GenderLess
        else -> CharacterGender.Unknown
    }

    val characterStatus : CharacterStatus = when (status.lowercase()){
        "alive" -> CharacterStatus.Alive
        "dead" -> CharacterStatus.Dead
        else -> CharacterStatus.Unknown
    }

    return Character(
        id = id,
        name = name,
        status = characterStatus,
        gender = characterGender,
        imageUrl = image,
        location = Character.Location(location.name, location.url),
        origin = Character.Origin(origin.name),
        species = species,
        type = type,
        created = created,
        episodeUrls = episode.map { it.split("/").last().toInt() }
    )
}