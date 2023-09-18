package com.xs.firenotesapp.ui.screens.homescreen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.xs.firenotesapp.ui.components.MySnackBar
import com.xs.firenotesapp.ui.screens.homescreen.HomeScreenEvent
import com.xs.firenotesapp.ui.screens.homescreen.NewsListItem
import com.xs.firenotesapp.ui.screens.homescreen.viewmodel.HomeScreenViewModel
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNewsScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavHostController
){
    val snackBarHostState = remember { SnackbarHostState() }
    val newsState by viewModel.newsState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true, block ={
        viewModel.uiEvent.collectLatest {
            when(it){
                is UiEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                UiEvent.PopBackStack -> TODO()
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(it.message, it.action, duration = SnackbarDuration.Short)
                }
            }
        }
    })


    Scaffold(modifier = Modifier,
        snackbarHost = {
            MySnackBar(snackBarHostState = snackBarHostState) {
            }
        }
        ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            when {
                newsState.isLoading -> {
                    Column(modifier = Modifier.fillMaxSize(),
                        Arrangement.Center,
                        Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 1.dp
                        )
                    }
                }

                newsState.data.isNullOrEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        Arrangement.Center,
                        Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 1.dp
                        )
                        Text(text = "No News Available, Sorry")
                    }
                }

                !newsState.data.isNullOrEmpty() -> {
                    LazyColumn(modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(), content = {
                        items(newsState.data!!) { newsData ->
                            NewsListItem(
                                headline = newsData!!.headline,
                                description = newsData.description,
                                author = newsData.author,
                                date = newsData.date,
                                onDelete = {
                                    viewModel.onViewModelEvent(HomeScreenEvent.OnDelete(newsData))
                                },
                                shimmer = false
                            )
                        }
                    })
                }

            }

        }
    }
}