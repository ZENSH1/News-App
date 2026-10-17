package com.xs.newsapp.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xs.newsapp.R
import com.xs.newsapp.core.ui.components.ArticleCard
import com.xs.newsapp.core.ui.components.LoadingContent
import com.xs.newsapp.core.ui.components.MessageContent
import com.xs.newsapp.core.ui.components.messageRes
import com.xs.newsapp.core.ui.components.plusAll
import com.xs.newsapp.core.ui.components.rememberArticleDateFormatter
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.data.model.Article

@Composable
fun FeedScreen(viewModel: FeedViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FeedContent(state = state, onIntent = viewModel::onIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    state: FeedState,
    onIntent: (FeedIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.feed_title)) }) },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding)
        when {
            state.articles.isNotEmpty() -> ArticleList(
                articles = state.articles,
                contentPadding = innerPadding.plusAll(16.dp),
                modifier = contentModifier,
            )
            state.isLoading -> LoadingContent(contentModifier.padding(innerPadding))
            state.error != null -> MessageContent(
                message = stringResource(state.error.messageRes()),
                actionLabel = stringResource(R.string.retry),
                onAction = { onIntent(FeedIntent.Retry) },
                modifier = contentModifier.padding(innerPadding),
            )
            else -> MessageContent(
                message = stringResource(R.string.feed_empty),
                modifier = contentModifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun ArticleList(
    articles: List<Article>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = rememberArticleDateFormatter()
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(articles, key = { it.id }, contentType = { "article" }) { article ->
            ArticleCard(article = article, dateFormatter = dateFormatter)
        }
    }
}

@Preview
@Composable
private fun FeedContentPreview() {
    NewsTheme {
        FeedContent(
            state = FeedState(
                isLoading = false,
                articles = listOf(
                    Article("1", "Local team reaches the final", "A late goal sealed the win.", "u1", "Ayesha", 0),
                    Article("2", "Library opens a study hall", "Open around the clock.", "u2", "Bilal", 0),
                ),
            ),
            onIntent = {},
        )
    }
}
