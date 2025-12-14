package com.akshat.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.akshat.network.KtorClient
import com.akshat.network.data.domain.Character
import com.akshat.rickandmorty.ui.screens.CharacterDetailScreen
import com.akshat.rickandmorty.ui.screens.EpisodeScreen
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
                ScaffoldAppUI(17)
            }
        }
    }


    @Composable
    fun ScaffoldAppUI(characterId: Int) {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { RickAndMortyTopAppBar() }) { innerPadding ->
            Surface(modifier = Modifier.padding(paddingValues = innerPadding)) {
                NavHost(navController = navController,
                    startDestination = "detailScreen"){
                    composable(route = "detailScreen") {
                        CharacterDetailScreen(ktorClient = client,
                            characterId=characterId,
                            onEpisodeClick = { navController.navigate("episodes/$characterId") } )
                    }
                    // Need to call out the arguments being passed in the route
                    composable (route="episodes/{characterId}",
                        arguments = listOf(navArgument("characterId") {type = NavType.IntType})) { backstackEntry ->
                        val characterId = backstackEntry.arguments?.getInt("characterId") ?: 0
                        EpisodeScreen(characterId = characterId)
                    }
                }
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