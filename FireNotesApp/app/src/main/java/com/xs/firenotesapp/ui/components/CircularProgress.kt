package com.xs.firenotesapp.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xs.firenotesapp.R

@Composable
@Preview
fun mycircular(){

    Column(
        Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {


        CircularProgressIndicator(
            modifier = Modifier
                .padding(10.dp),

            color = colorResource(id = R.color.blue),
            strokeWidth = 3.dp
        )
    }
}