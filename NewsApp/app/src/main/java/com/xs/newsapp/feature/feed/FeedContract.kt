package com.xs.newsapp.feature.feed

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendError

data class FeedState(
    val isLoading: Boolean = true,
    val articles: List<Article> = emptyList(),
    val error: BackendError? = null,
)

sealed interface FeedIntent {
    data object Retry : FeedIntent
}

sealed interface FeedMutation {
    data object LoadingStarted : FeedMutation
    data class ArticlesLoaded(val articles: List<Article>) : FeedMutation
    data class LoadingFailed(val error: BackendError) : FeedMutation
}

object FeedReducer : Reducer<FeedState, FeedMutation> {
    override fun reduce(state: FeedState, mutation: FeedMutation): FeedState = when (mutation) {
        // Keep showing what we have while reloading; only the spinner and error change.
        FeedMutation.LoadingStarted -> state.copy(isLoading = true, error = null)
        is FeedMutation.ArticlesLoaded -> FeedState(isLoading = false, articles = mutation.articles)
        is FeedMutation.LoadingFailed -> state.copy(isLoading = false, error = mutation.error)
    }
}
