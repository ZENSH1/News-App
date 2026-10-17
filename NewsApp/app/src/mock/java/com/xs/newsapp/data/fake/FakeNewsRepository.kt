package com.xs.newsapp.data.fake

import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

/** Mock flavor: stories live in [InMemoryBackend] and every change is pushed to observers, like Firestore. */
class FakeNewsRepository @Inject constructor(
    private val backend: InMemoryBackend,
) : NewsRepository {

    override fun observeArticles(): Flow<List<Article>> =
        backend.articles.map { articles -> articles.sortedByDescending { it.publishedAtEpochMillis } }

    override suspend fun publish(headline: String, description: String, author: User) {
        delay(NETWORK_LATENCY_MILLIS)
        val article = Article(
            id = UUID.randomUUID().toString(),
            headline = headline,
            description = description,
            authorId = author.id,
            authorName = author.name,
            publishedAtEpochMillis = System.currentTimeMillis(),
        )
        backend.articles.update { it + article }
    }

    override suspend fun delete(articleId: String) {
        delay(NETWORK_LATENCY_MILLIS)
        if (backend.articles.value.none { it.id == articleId }) throw BackendException(BackendError.NotFound)
        backend.articles.update { articles -> articles.filterNot { it.id == articleId } }
    }
}
