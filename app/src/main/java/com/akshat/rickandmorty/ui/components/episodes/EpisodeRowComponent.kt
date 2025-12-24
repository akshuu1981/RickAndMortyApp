package com.akshat.rickandmorty.ui.components.episodes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshat.network.data.domain.Episode
import com.akshat.rickandmorty.ui.theme.Purple80

@Composable
fun EpisodeRowComponent(episode: Episode){
    Row(modifier = Modifier.fillMaxWidth()
        .padding(16.dp)
        ){
        Column(modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.Start) {
            Text(
                text = "Episode",
                fontSize = 14.sp,
                color = Purple80,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Text(
                text = episode.episodeNumber.toString(),
                fontSize = 20.sp,
                color = Purple80,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

        }
        Column(modifier = Modifier.weight(1.5f),
            horizontalAlignment = Alignment.End
            ) {
            Text(
                text = episode.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Purple80,
                textAlign = TextAlign.End
            )
            Text(
                text = episode.airDate,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Purple80,
                textAlign = TextAlign.End
            )
        }
    }
}


@Preview
@Composable
fun PreviewEpisodeRowComponent(){
    EpisodeRowComponent(Episode(1,
        "Pilot",
        "December 2, 2013",
        1,
        1,
        listOf(1,2)))
}


@Preview
@Composable
fun PreviewEpisodeRowComponentLongName(){
    EpisodeRowComponent(Episode(1,
        "Pilot a very long title name and a really long title",
        "December 2, 2013",
        1,
        1,
        listOf(1,2)))
}