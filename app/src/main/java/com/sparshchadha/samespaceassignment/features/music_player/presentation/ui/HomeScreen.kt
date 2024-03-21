package com.sparshchadha.samespaceassignment.features.music_player.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongDetails
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongsDto

@Composable
fun HomeScreen(songs: SongsDto?) {
    Scaffold(
        bottomBar = {
            HomeScreenBottomBar(
                listOf(
                    BottomBarItems(title = "For You", isSelected = true),
                    BottomBarItems(title = "Top Tracks", isSelected = false)
                )
            )
        },
        containerColor = Color.Black
    ) { innerPaddingValues ->
        if (songs != null) {
            LazyColumn (
                modifier = Modifier.padding(top = innerPaddingValues.calculateTopPadding())
            ) {
                items(songs.data) {
                    Song(songDetails = it)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        } else {

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Song(songDetails: SongDetails) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        val imageUrl = "https://cms.samespace.com/assets/${songDetails.cover}"
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
        ) {
            Text(
                text = songDetails.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )

            Text(
                text = songDetails.artist,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun HomeScreenBottomBar(
    bottomBarItems: List<BottomBarItems>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        bottomBarItems.forEach {
            if (it.isSelected) {
                SelectedBottomBarItem(
                    title = it.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                Text(
                    text = it.title,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SelectedBottomBarItem(
    title: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Canvas(
            modifier = Modifier
                .size(5.dp)
        ) {
            drawCircle(color = Color.White)
        }
    }
}

data class BottomBarItems(
    val title: String,
    val isSelected: Boolean
)

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(null)
}
