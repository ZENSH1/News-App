package com.xs.newsapp.data.repository

import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.User
import kotlinx.coroutines.flow.Flow

/** Main-safe; throws only [BackendException]. */
interface NewsRepository {

    /** Newest first, live. */
    fun observeArticles(): Flow<List<Article>>

    suspend fun publish(headline: String, description: String, author: User)

    suspend fun delete(articleId: String)
}
