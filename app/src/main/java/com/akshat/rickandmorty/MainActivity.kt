package com.akshat.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akshat.network.KtorClient
import com.akshat.network.data.domain.Character
import com.akshat.rickandmorty.ui.screens.CharacterDetailScreen
import com.akshat.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {

    private val client: KtorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var character by remember {
                mutableStateOf<Character?>(null)
            }
            RickAndMortyTheme {
                ScaffoldAppUI(1)
            }
        }
    }


    @Composable
    fun ScaffoldAppUI(characterId: Int) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { RickAndMortyTopAppBar() }) { innerPadding ->
            Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
                CharacterDetailScreen(client, characterId = characterId)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RickAndMortyTopAppBar() {
        TopAppBar(
            title = {
                Text("Rick and Morty")
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ScaffoldAppUI(1)
    }

}