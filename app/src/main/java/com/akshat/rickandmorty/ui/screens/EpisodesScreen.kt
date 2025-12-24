package com.akshat.rickandmorty.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshat.network.KtorClient
import com.akshat.network.data.domain.Character
import com.akshat.network.data.domain.CharacterGender
import com.akshat.network.data.domain.CharacterStatus
import com.akshat.network.data.domain.Episode
import com.akshat.rickandmorty.ui.components.character.DataPointComponent
import com.akshat.rickandmorty.ui.components.episodes.EpisodeRowComponent
import com.akshat.rickandmorty.data.DataPoint
import com.akshat.rickandmorty.ui.theme.Pink80
import kotlinx.coroutines.launch

@Composable
fun EpisodeScreen(characterId: Int, client: KtorClient) {

    var characterState by remember { mutableStateOf<Character?>(null) }
    var episodeState by remember { mutableStateOf<List<Episode>>(emptyList()) }

    LaunchedEffect(Unit) {
        client.getCharacter(characterId).onSuccess { character ->
            characterState = character
            launch {
                client.getEpisodes(character.episodeUrls).onSuccess { episodes ->
                    episodeState = episodes
                }.onFailure {e ->
                    Log.d("EpisodeScreen", "Error getting episodes : $e")
                }
            }
        }.onFailure {e->
            Log.d("EpisodeScreen", "Error getting characters : $e")
        }
    }

    characterState?.let {
        EpisodeListScreen(character = it,episodes = episodeState)
    } ?: LoadingState()

}

@Composable
fun EpisodeListScreen(character: Character, episodes: List<Episode>) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item { CharacterNameComponent(character.name) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            LazyRow {
                episodeBySeasonMap.forEach { entry ->
                  val title = "Season ${entry.key}"
                  val description = "${entry.value.size} ep"
                    item {
                        DataPointComponent(dataPoint = DataPoint(title, description))
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { CharacterImage(character) }
        item { Spacer(modifier = Modifier.height(32.dp)) }

        episodeBySeasonMap.forEach { entry ->
            stickyHeader { SeasonHeader(entry.key) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(entry.value){episode ->
                EpisodeRowComponent(episode)
            }
        }
    }
}

@Composable
fun SeasonHeader(seasonNumber: Int){
    Text(
        text = "Season $seasonNumber",
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Pink80, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp)
            .background(color = Pink80.copy(alpha = 0.5f)),
        fontSize = 32.sp,
        lineHeight = 32.sp,
        color = Pink80,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun PreviewEpisodeListScreen(){
    EpisodeListScreen(
        Character(
            id = 1,
            name = "Rick Sanchez",
            origin = Character.Origin("origin"),
            created = "created",
            episodeUrls = listOf(1, 2, 3),
            gender = CharacterGender.Male,
            status = CharacterStatus.Alive,
            imageUrl = "image",
            location = Character.Location("location", "url"),
            species = "species",
            type = "type"),
        episodes = listOf(Episode(1,
            "Pilot",
            "December 2, 2013",
            1,
            1,
            listOf(1,2)),
            Episode(2,
                "We go to Market",
                "December 14, 2013",
                1,
                2,
                listOf(1,2))
    ))
}