package com.xs.firenotesapp.ui.screens.homescreen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xs.firenotesapp.ui.screens.homescreen.HomeScreenEvent
import com.xs.firenotesapp.ui.screens.homescreen.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostNewsScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
){
    Scaffold(
    ) {
        Column(Modifier.padding(it).fillMaxSize(),
            Arrangement.spacedBy(5.dp),
            Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = viewModel.headLine,
                onValueChange = { value ->
                    viewModel.onViewModelEvent(HomeScreenEvent.OnChangeHeadline(value))
                },
                placeholder = {
                    Text(text = "Headline")
                }
            )
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { value ->
                    viewModel.onViewModelEvent(HomeScreenEvent.OnChangeDescription(value))
                },
                placeholder = {
                    Text(text = "Description")
                }
            )

            Button(onClick = { viewModel.onViewModelEvent(HomeScreenEvent.PostNews) }) { Text(text = "Post The News") }

        }
    }

}