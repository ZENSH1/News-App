package com.xs.newsapp.feature.feed

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/** The feed has no effects: loading, content and errors are all part of [FeedState]. */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : MviViewModel<FeedState, FeedIntent, FeedMutation, Nothing>(FeedState(), FeedReducer) {

    private var observeJob: Job? = null

    init {
        observeArticles()
    }

    override fun handleIntent(intent: FeedIntent) {
        when (intent) {
            FeedIntent.Retry -> observeArticles()
        }
    }

    /** Subscribes to the real-time list. Every new snapshot becomes an [FeedMutation.ArticlesLoaded]. */
    private fun observeArticles() {
        observeJob?.cancel()
        mutate(FeedMutation.LoadingStarted)
        observeJob = viewModelScope.launch {
            newsRepository.observeArticles()
                .catch { error ->
                    if (error is BackendException) mutate(FeedMutation.LoadingFailed(error.error)) else throw error
                }
                .collect { articles -> mutate(FeedMutation.ArticlesLoaded(articles)) }
        }
    }
}
