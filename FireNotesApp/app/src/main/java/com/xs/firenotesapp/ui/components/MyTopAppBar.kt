package com.xs.firenotesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xs.firenotesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MyTopAppBar(
    title:String = "Home Page",
    icon: ImageVector = Icons.Default.Home
) {
    Card(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(5.dp, hoveredElevation = 30.dp)
    ) {
        TopAppBar(
            title = { Text(text = title, color = colorResource(id = R.color.blue)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            navigationIcon = { Icon(imageVector = icon,
                contentDescription = "Icon",
                tint = colorResource(id = R.color.blue)) },
            actions = {},
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.White),
            scrollBehavior = null
        )
    }

}