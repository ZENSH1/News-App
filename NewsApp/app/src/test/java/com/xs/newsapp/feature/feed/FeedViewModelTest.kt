package com.xs.newsapp.feature.feed

import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestNewsRepository
import com.xs.newsapp.testing.article
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun showsTheStories_andFollowsLiveUpdates() = runTest {
        val newsRepository = TestNewsRepository(article("1"))
        val viewModel = FeedViewModel(newsRepository)
        assertEquals(FeedState(isLoading = false, articles = listOf(article("1"))), viewModel.state.value)

        newsRepository.setArticles(article("2"), article("1"))

        assertEquals(listOf("2", "1"), viewModel.state.value.articles.map { it.id })
    }

    @Test
    fun loadingFailure_showsTheError_andRetryRecovers() = runTest {
        val newsRepository = TestNewsRepository(article("1")).apply { observeFailure = BackendError.Network }
        val viewModel = FeedViewModel(newsRepository)
        assertEquals(FeedState(isLoading = false, error = BackendError.Network), viewModel.state.value)

        newsRepository.observeFailure = null
        viewModel.onIntent(FeedIntent.Retry)

        assertEquals(FeedState(isLoading = false, articles = listOf(article("1"))), viewModel.state.value)
    }
}
