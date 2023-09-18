package com.xs.firenotesapp.ui.screens.homescreen.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.xs.firenotesapp.R
import com.xs.firenotesapp.data.news.NewsData
import com.xs.firenotesapp.ui.components.MySnackBar
import com.xs.firenotesapp.ui.components.mycircular
import com.xs.firenotesapp.ui.screens.homescreen.HomeScreenEvent
import com.xs.firenotesapp.ui.screens.homescreen.NewsListItem
import com.xs.firenotesapp.ui.screens.homescreen.viewmodel.HomeScreenViewModel
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val newsState by viewModel.newsState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.navigate(
                        it.route,
                        navOptions = NavOptions.Builder().setPopUpTo(it.route, false).build(),
                        navigatorExtras = null
                    )
                }

                UiEvent.PopBackStack -> TODO()
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        it.message,
                        it.action,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    })

    Scaffold(
        snackbarHost = {
            MySnackBar(snackBarHostState = snackBarHostState) {

            }
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            Arrangement.SpaceEvenly,
            Alignment.CenterHorizontally
        ) {
            Card(
                Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(5.dp)) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Text(text = "Name: ${viewModel.currentUser?.displayName}")
                            Text(text = "Email: ${viewModel.currentUser?.email}")
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            viewModel.onViewModelEvent(HomeScreenEvent.Logout)
                            navController.navigate(
                                Routes.AuthScreen,
                                navOptions = NavOptions.Builder()
                                    .setPopUpTo(Routes.AuthScreen, false)
                                    .build()
                            )
                        }) {
                            Text(text = "Logout")
                        }
                        Button(onClick = {
                            navController.navigate(
                                Routes.ProfileScreen
                            )
                        }) {
                            Text(text = "View Profile")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Row {
                Text(text = "------")
                Text(text = "Your Posts", color = colorResource(id = R.color.blueInk))
                Text(text = "------")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            //The List Logic
            when {
                newsState.isLoading -> {
                    Column(
                        modifier = Modifier.weight(1f, true),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        mycircular()
                        Text(text = "Loading Your Posts Please Wait...")
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
                        Text(text = "Its Empty, Sorry")
                    }
                }

                !newsState.data.isNullOrEmpty() -> {
                    lateinit var x: List<NewsData?>
                    if (viewModel.currentUser != null) {
                        x = try {
                            newsState.data!!.filter { newsData -> newsData?.userID.equals(viewModel.currentUser?.uid) }
                        } catch (e: Exception) {
                            listOf(NewsData("T", "E", "S", "T", "I", "NG"))
                        }
                        LazyColumn(modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize(), content = {
                            items(x) { newsData ->
                                NewsListItem(
                                    headline = newsData!!.headline,
                                    description = newsData.description,
                                    author = newsData.author,
                                    date = newsData.date,
                                    onDelete = {
                                        viewModel.onViewModelEvent(
                                            HomeScreenEvent.OnDelete(
                                                newsData
                                            )
                                        )
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

}