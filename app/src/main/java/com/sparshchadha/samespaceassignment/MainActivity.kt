package com.sparshchadha.samespaceassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.samespaceassignment.features.music_player.presentation.ui.HomeScreen
import com.sparshchadha.samespaceassignment.features.music_player.presentation.viewmodels.MusicPlayerViewModel
import com.sparshchadha.samespaceassignment.ui.theme.SamespaceAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SamespaceAssignmentTheme {
                when(val songs = musicPlayerViewModel.allSongs.collectAsState().value) {
                    is Resource.Loading -> {
                        CustomLoader()
                    }
                    is Resource.Success -> {
                        HomeScreen(
                            songs = songs.data
                        )
                    }
                    is Resource.Error -> {
                        ErrorDuringFetch(errorMessage = "Unable To Get Songs!")
                    }
                    else -> {

                    }
                }

            }
        }
    }

    @Composable
    fun CustomLoader() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
            val animProgress by animateLottieCompositionAsState(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = lottieComposition,
                progress = { animProgress },
                modifier = Modifier.size(dimensionResource(id = R.dimen.progress_bar_size)),
            )
        }
    }

    @Composable
    fun ErrorDuringFetch(errorMessage: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_error_24),
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(60.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.White,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "No Results Found!",
                    color = Color.White,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
