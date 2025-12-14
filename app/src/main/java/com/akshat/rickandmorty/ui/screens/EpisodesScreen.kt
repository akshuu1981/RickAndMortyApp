package com.akshat.rickandmorty.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EpisodeScreen(characterId: Int) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Episodes")
        Text("Episodes for character $characterId")
        Text("Episodes")
    }
}