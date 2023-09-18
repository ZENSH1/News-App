package com.xs.firenotesapp.ui.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material.placeholder
import com.xs.firenotesapp.R

@Composable
fun NewsListItem(headline: String, description: String,author:String,date:String, onDelete: () -> Unit, shimmer:Boolean = false) {
    Card(modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color.White)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(
                text = headline, modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(shimmer),
                color = colorResource(id = R.color.blue),
                style = MaterialTheme.typography.titleMedium
            )
            Row(Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically) {

                Text(
                    text = "By: $author",
                    modifier = Modifier
                        .placeholder(shimmer),
                    color = colorResource(id = R.color.blueInk),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Time: $date",
                    modifier = Modifier
                        .placeholder(shimmer),
                    color = colorResource(id = R.color.blueInk),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = description, modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(shimmer),
                color = colorResource(id = R.color.blueGrey),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}