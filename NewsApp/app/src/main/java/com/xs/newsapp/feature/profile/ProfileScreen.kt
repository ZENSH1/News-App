package com.xs.newsapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xs.newsapp.R
import com.xs.newsapp.core.mvi.ObserveEffects
import com.xs.newsapp.core.ui.components.ArticleCard
import com.xs.newsapp.core.ui.components.LoadingContent
import com.xs.newsapp.core.ui.components.MessageContent
import com.xs.newsapp.core.ui.components.messageRes
import com.xs.newsapp.core.ui.components.plusAll
import com.xs.newsapp.core.ui.components.rememberArticleDateFormatter
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.UserProfile
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            ProfileEffect.NavigateToEditProfile -> onEditProfile()
            ProfileEffect.ArticleDeleted -> scope.launch {
                snackbarHostState.showSnackbar(resources.getString(R.string.article_deleted))
            }
            is ProfileEffect.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(resources.getString(effect.error.messageRes()))
            }
        }
    }

    ProfileContent(state = state, onIntent = viewModel::onIntent, snackbarHostState = snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile_title)) },
                actions = {
                    IconButton(
                        onClick = { onIntent(ProfileIntent.SignOutClicked) },
                        enabled = !state.isSigningOut,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.sign_out))
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val profile = state.profile
        when {
            state.isLoading -> LoadingContent(Modifier.padding(innerPadding))
            profile == null -> MessageContent(
                message = stringResource((state.error ?: return@Scaffold).messageRes()),
                modifier = Modifier.padding(innerPadding),
            )
            else -> Box(
                Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding),
            ) {
                ProfileList(
                    profile = profile,
                    articles = state.articles,
                    onIntent = onIntent,
                    contentPadding = innerPadding.plusAll(16.dp),
                )
                if (state.isDeleting || state.isSigningOut) {
                    LinearProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                    )
                }
            }
        }
    }

    // A dialog is just more state: it is open exactly while articlePendingDeletion is set.
    state.articlePendingDeletion?.let { article ->
        AlertDialog(
            onDismissRequest = { onIntent(ProfileIntent.DeleteDismissed) },
            title = { Text(stringResource(R.string.delete_article_title)) },
            text = { Text(stringResource(R.string.delete_article_message, article.headline)) },
            confirmButton = {
                TextButton(onClick = { onIntent(ProfileIntent.DeleteConfirmed) }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(ProfileIntent.DeleteDismissed) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun ProfileList(
    profile: UserProfile,
    articles: List<Article>,
    onIntent: (ProfileIntent) -> Unit,
    contentPadding: androidx.compose.foundation.layout.PaddingValues,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = rememberArticleDateFormatter()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "header", contentType = "header") {
            ProfileHeader(
                profile = profile,
                postCount = articles.size,
                onEditClick = { onIntent(ProfileIntent.EditProfileClicked) },
            )
        }
        item(key = "posts-title", contentType = "title") {
            Text(
                text = stringResource(R.string.profile_your_posts),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        if (articles.isEmpty()) {
            item(key = "no-posts", contentType = "message") {
                Text(
                    text = stringResource(R.string.profile_no_posts),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(articles, key = { it.id }, contentType = { "article" }) { article ->
            ArticleCard(
                article = article,
                dateFormatter = dateFormatter,
                onDelete = { onIntent(ProfileIntent.DeleteArticleClicked(article)) },
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile,
    postCount: Int,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = profile.name.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Column {
                    Text(text = profile.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = profile.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(stringResource(R.string.profile_role, profile.role), style = MaterialTheme.typography.bodyMedium)
            profile.age?.let { age ->
                Text(stringResource(R.string.profile_age, age), style = MaterialTheme.typography.bodyMedium)
            }
            Text(stringResource(R.string.profile_posts, postCount), style = MaterialTheme.typography.bodyMedium)
            OutlinedButton(onClick = onEditClick, modifier = Modifier.align(Alignment.End)) {
                Icon(Icons.Filled.Edit, contentDescription = null)
                Text(stringResource(R.string.edit_profile), modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    NewsTheme {
        ProfileContent(
            state = ProfileState(
                isLoading = false,
                profile = UserProfile("demo", "Demo Reporter", "demo@newsapp.test", "Reporter", age = 27),
                articles = listOf(Article("1", "Local team reaches the final", "A late goal.", "demo", "Demo Reporter", 0)),
            ),
            onIntent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
