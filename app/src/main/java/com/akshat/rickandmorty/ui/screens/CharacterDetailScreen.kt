package com.akshat.rickandmorty.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.akshat.network.KtorClient
import com.akshat.network.data.domain.Character
import com.akshat.network.data.domain.CharacterStatus
import com.akshat.rickandmorty.components.character.CharacterStatusComponent
import com.akshat.rickandmorty.components.character.DataPointComponent
import com.akshat.rickandmorty.data.DataPoint
import com.akshat.rickandmorty.ui.theme.Pink80
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun CharacterDetailScreen(
    ktorClient: KtorClient,
    characterId : Int,
    onEpisodeClick: (Int) -> Unit
){

    var character by remember { mutableStateOf<Character?>(null) }

    val characterDataPoints: List<DataPoint> by remember {
        derivedStateOf {
            buildList {
                character?.let { character ->
                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.gender))
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode count", character.episodeUrls.size.toString()))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        val apiOperation = ktorClient.getCharacter(characterId)
            .onSuccess {
                character = it
            }
            .onFailure { exception ->

            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        if(character == null){
            item { LoadingState() }
            return@LazyColumn
        }
        item {
            CharacterDetailsNamePlateComponent(
                name = character!!.name,
                status = character!!.status
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Image
        item {
            CharacterImage(character)
        }
        // Details about the character
        items(characterDataPoints) { points ->
            Spacer(modifier = Modifier.height(32.dp))
            DataPointComponent(dataPoint = points)
        }

        item { Spacer(modifier = Modifier.height(32.dp))}

        // Button
        item {
            Button(
                onClick = { onEpisodeClick(characterId) },
                modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()

            ) {
                Text("View all episodes")
            }
        }

        item { Spacer(modifier = Modifier.height(64.dp))}

    }
}

@Composable
fun CharacterImage(character: Character?) {
    SubcomposeAsyncImage(
        model = character!!.imageUrl, /*ImageRequest.Builder(LocalContext.current)
                    .data(character?.imageUrl)
                    .crossfade(true)
                    .build(),*/
        //placeholder = painterResource(R.drawable.placeholder),
        contentDescription = "image of ${character.name}",
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .aspectRatio(1f),
        loading = {
            Log.d("CharacterDetail","Loading image : ${character.imageUrl}")
            LoadingState() },
        error = { e->
            Log.d("CharacterDetail","Error loading image : ${e.result.throwable}")
        }
    )
}

@Composable
fun LoadingState() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize()
            .padding(128.dp),
        color = Pink80
    )
}

@Composable
fun CharacterDetailsNamePlateComponent(name: String, status: CharacterStatus) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CharacterStatusComponent(status)
        CharacterNameComponent(name)
    }
}

@Composable
fun CharacterNameComponent(name: String) {
    Text(text = name, fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Pink80)
}

@Preview
@Composable
fun PreviewCharacterDetailsNamePlateComponentAlive(){
    CharacterDetailsNamePlateComponent("Rick S", CharacterStatus.Alive)
}

@Preview
@Composable
fun PreviewCharacterDetailScreen(){
    CharacterDetailScreen(KtorClient(),1, onEpisodeClick = {})
}