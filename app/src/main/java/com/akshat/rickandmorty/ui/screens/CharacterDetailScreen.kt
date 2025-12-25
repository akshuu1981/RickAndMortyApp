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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import com.akshat.network.data.domain.Character
import com.akshat.network.data.domain.CharacterStatus
import com.akshat.rickandmorty.state.CharacterDetailViewState
import com.akshat.rickandmorty.ui.components.character.CharacterStatusComponent
import com.akshat.rickandmorty.ui.components.character.DataPointComponent
import com.akshat.rickandmorty.ui.theme.Pink80

@Composable
fun CharacterDetailScreen(
    characterId : Int,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onEpisodeClick: (Int) -> Unit
){

    LaunchedEffect(Unit) {
       viewModel.fetchCharacter(characterId)
    }
    val state by viewModel.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        when (val viewState = state) {
            is CharacterDetailViewState.Loading -> {
                item { LoadingState() }
            }

            is CharacterDetailViewState.Error -> { }
            is CharacterDetailViewState.Success -> {

                item {
                    CharacterDetailsNamePlateComponent(
                        name = viewState.character.name,
                        status = viewState.character.status
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Image
                item {
                    CharacterImage(viewState.character)
                }
                // Details about the character
                items(viewState.datapoints) { points ->
                    Spacer(modifier = Modifier.height(32.dp))
                    DataPointComponent(dataPoint = points)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // Button
                item {
                    Button(
                        onClick = { onEpisodeClick(characterId) },
                        modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()

                    ) {
                        Text("View all episodes")
                    }
                }

                item { Spacer(modifier = Modifier.height(64.dp)) }

            }
        }
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
    CharacterDetailScreen(1, viewModel(), onEpisodeClick = {})
}