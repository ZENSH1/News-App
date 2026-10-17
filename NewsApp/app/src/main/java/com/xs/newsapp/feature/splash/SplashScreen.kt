package com.xs.newsapp.feature.splash

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.xs.newsapp.R
import kotlinx.coroutines.delay

private const val ANIMATION_START_DELAY_MILLIS = 300L
private const val ANIMATION_AND_HOLD_MILLIS = 1_900L

/** Plays the logo animation, then calls [onFinished]. Where to go next is up to [com.xs.newsapp.app.AppState]. */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SplashScreen(onFinished: () -> Unit, modifier: Modifier = Modifier) {
    val logo = AnimatedImageVector.animatedVectorResource(R.drawable.lion_to_my_name_perfected)
    var atEnd by rememberSaveable { mutableStateOf(false) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        delay(ANIMATION_START_DELAY_MILLIS)
        atEnd = true
        delay(ANIMATION_AND_HOLD_MILLIS)
        currentOnFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            // The logo is drawn for white, even in dark theme.
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = rememberAnimatedVectorPainter(logo, atEnd),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
