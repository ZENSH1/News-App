@file:OptIn(ExperimentalAnimationGraphicsApi::class)

package com.xs.firenotesapp.ui.screens.splashscreen

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.xs.firenotesapp.R
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is UiEvent.Navigate -> onNavigate(it)
                UiEvent.PopBackStack -> {

                }

                is UiEvent.ShowSnackBar -> {

                }

            }
        }
    })

    Animatedmylogo(viewModel.logoMovement)
}

@Preview
@Composable
fun Animatedmylogo(atEnd: Boolean = true) {

    val image = AnimatedImageVector.animatedVectorResource(R.drawable.lion_to_my_name_perfected)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = "Timer",
            modifier = Modifier.background(color = Color.Transparent),
            contentScale = ContentScale.FillWidth
        )
    }

}