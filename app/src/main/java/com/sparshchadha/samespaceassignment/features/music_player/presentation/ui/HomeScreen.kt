package com.sparshchadha.samespaceassignment.features.music_player.presentation.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.sparshchadha.samespaceassignment.R
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongDetails
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.dto.SongsDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


@Composable
fun HomeScreen(songs: SongsDto?) {
    var isSongPlaying by rememberSaveable {
        mutableStateOf(false)
    }
    var playingSong by rememberSaveable {
        mutableStateOf<SongDetails?>(null)
    }
    Scaffold(
        bottomBar = {
            HomeScreenBottomBar(
                bottomBarItems = listOf(
                    BottomBarItems(title = "For You", isSelected = true),
                    BottomBarItems(title = "Top Tracks", isSelected = false)
                ),
                isSongPlaying = isSongPlaying,
                playingSongDetails = playingSong
            )
        },
        containerColor = Color.Black
    ) { innerPaddingValues ->
        if (songs != null) {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = innerPaddingValues.calculateBottomPadding())
            ) {
                items(songs.data) {
                    Song(
                        songDetails = it,
                        onClick = {
                            isSongPlaying = true
                            playingSong = it
                        }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_padding)))
                }
            }
        } else {

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Song(
    songDetails: SongDetails,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = "https://cms.samespace.com/assets/${songDetails.cover}"
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
                .size(dimensionResource(id = R.dimen.circular_cover_size))
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenBottomBar(
    bottomBarItems: List<BottomBarItems>,
    isSongPlaying: Boolean,
    playingSongDetails: SongDetails?
) {
    if (isSongPlaying && playingSongDetails != null) {
        val imageUrl = "https://cms.samespace.com/assets/${playingSongDetails.cover}"
        var backgroundColor by remember {
            mutableStateOf(Color.Black)
        }
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(key1 = imageUrl) {
            coroutineScope.launch {
                backgroundColor = getDominantColor(imageUrl = imageUrl)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.medium_padding))
                        .size(dimensionResource(id = R.dimen.circular_cover_size))
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = playingSongDetails.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                        .weight(0.6f)
                )

                Box(
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.medium_padding)
                        )
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(dimensionResource(id = R.dimen.medium_padding))
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
            }
            HomeBottomBar(
                bottomBarItems = bottomBarItems
            )
        }
    } else {
        HomeBottomBar(
            bottomBarItems = bottomBarItems
        )
    }
}

suspend fun getDominantColor(imageUrl: String): Color {
    return try {
        withContext(Dispatchers.IO) {
            val url = URL(imageUrl)
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val palette = Palette.from(image).generate()
            val dominantSwatch = palette.dominantSwatch ?: palette.lightVibrantSwatch
            ?: palette.lightMutedSwatch ?: palette.vibrantSwatch
            ?: palette.mutedSwatch ?: palette.darkVibrantSwatch
            ?: palette.darkMutedSwatch
            rgbToColor(dominantSwatch?.rgb)
        }
    } catch (e: IOException) {
        Color.Black
    }
}

fun rgbToColor(rgb: Int?): Color {
    if (rgb != null) {
        val red = (rgb shr 16) and 0xFF
        val green = (rgb shr 8) and 0xFF
        val blue = rgb and 0xFF
        return Color(red, green, blue)
    }
    return Color.Black
}

@Composable
fun HomeBottomBar(
    bottomBarItems: List<BottomBarItems>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.large_padding)),
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_padding)))
        Canvas(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.tab_indicator_size))
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
