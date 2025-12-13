package com.akshat.network.data.domain

sealed class CharacterGender(val gender: String) {
    object Male : CharacterGender("Male")
    object Female : CharacterGender("Female")
    object GenderLess:  CharacterGender("No Gender")
    object Unknown: CharacterGender("Unknown")
}