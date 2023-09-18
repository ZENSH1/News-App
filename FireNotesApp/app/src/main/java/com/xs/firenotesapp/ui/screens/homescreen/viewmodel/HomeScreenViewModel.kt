package com.xs.firenotesapp.ui.screens.homescreen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.xs.firenotesapp.data.auth.AuthRepository
import com.xs.firenotesapp.data.news.NewsData
import com.xs.firenotesapp.data.news.NewsRepository
import com.xs.firenotesapp.ui.screens.homescreen.HomeScreenEvent
import com.xs.firenotesapp.ui.screens.homescreen.state.FirestoreNewsState
import com.xs.firenotesapp.utils.FirebaseResults
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import com.xs.firenotesapp.utils.localTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val newsState: StateFlow<FirestoreNewsState> = repository.getNews().map { result ->
        when (result) {
            is FirebaseResults.Failure -> FirestoreNewsState(errorMsg = result.exception.message)
            FirebaseResults.Loading -> FirestoreNewsState(isLoading = true)
            is FirebaseResults.Success -> FirestoreNewsState(data = result.result)
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = FirestoreNewsState(isLoading = true),
        started = SharingStarted.WhileSubscribed(5000)
    )

    var currentUser: FirebaseUser? = authRepository.currentUser!!

    init {
        if (currentUser == null) {
            sendUiEvent(UiEvent.Navigate(Routes.AuthScreen))
        }
    }


    var headLine by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set


    fun onViewModelEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnChangeDescription -> {
                description = event.value
            }

            is HomeScreenEvent.OnChangeHeadline -> {
                headLine = event.value
            }

            is HomeScreenEvent.PostNews -> {
                viewModelScope.launch {
                    repository.postNews(
                        NewsData(
                            description = description,
                            headline = headLine,
                            author = currentUser!!.displayName ?: "anonymous",
                            date = localTime(),
                            userID = currentUser!!.uid
                        )
                    )
                        .collect {
                            when (it) {
                                is FirebaseResults.Failure -> sendUiEvent(UiEvent.ShowSnackBar("${it.exception.message}"))
                                is FirebaseResults.Success -> {
                                    sendUiEvent(UiEvent.ShowSnackBar(it.result))
                                    headLine = ""
                                    description = ""
                                }

                                else -> {
                                    return@collect
                                }
                            }
                        }
                }
            }

            is HomeScreenEvent.OnDelete -> {
                viewModelScope.launch {
                    repository.deleteNews(event.newsData).collect {
                        when (it) {
                            is FirebaseResults.Failure -> sendUiEvent(UiEvent.ShowSnackBar("${it.exception.message}"))
                            is FirebaseResults.Success -> sendUiEvent(UiEvent.ShowSnackBar(it.result))
                            else -> {
                                return@collect
                            }
                        }
                    }
                }
            }

            HomeScreenEvent.Logout -> {
                authRepository.logout()
                sendUiEvent(UiEvent.Navigate(Routes.AuthScreen))
            }
        }

    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}

