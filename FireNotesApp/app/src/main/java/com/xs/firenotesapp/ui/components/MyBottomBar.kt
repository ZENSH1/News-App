package com.xs.firenotesapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xs.firenotesapp.R
import com.xs.firenotesapp.ui.theme.FireNotesAppTheme

@Composable
fun MyBottomBar(
    onViewClicked: () -> Unit,
    onPostClicked: () -> Unit,
    onProfileClicked: ()-> Unit,
    selected: Int = 0,
) {
    NavigationBar(
        tonalElevation = 20.dp,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        NavigationBarItem(selected = selected == 0,
            onClick = onViewClicked,
            icon = {
                if (selected == 0) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.blue
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.blueGrey
                        )
                    )
                }
            }, label = {
                AnimatedVisibility(visible = selected == 0) {
                    Text(text = "View News", color = colorResource(id = R.color.blue))
                }
            })
        NavigationBarItem(selected = selected == 1,
            onClick = onPostClicked,
            icon = {
                if (selected == 1) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.blue
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.blueGrey
                        )
                    )
                }
            },
            label = {
                AnimatedVisibility(visible = selected == 1) {
                    Text(text = "Post News", color = colorResource(id = R.color.blue))
                }
            })
        NavigationBarItem(selected = selected == 3,
            onClick = onProfileClicked,
            icon = {
                if (selected == 3) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.blue
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.blueGrey
                        )
                    )
                }
            },
            label = {
                AnimatedVisibility(visible = selected == 3) {
                    Text(text = "My Profile", color = colorResource(id = R.color.blue))
                }
            })

    }
}

@Composable
@Preview
fun PreviewBar(){
    FireNotesAppTheme {
        MyBottomBar(
            onViewClicked = { /*TODO*/ },
            onPostClicked = { /*TODO*/ },
            onProfileClicked = { /*TODO*/ })
    }
}