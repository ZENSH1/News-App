package com.xs.newsapp.data.repository

import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.User
import kotlinx.coroutines.flow.Flow

/** News stories. Suspend functions are main-safe and only throw [BackendException]. */
interface NewsRepository {

    /** Every story, newest first, updated in real time. Fails with [BackendException]. */
    fun observeArticles(): Flow<List<Article>>

    suspend fun publish(headline: String, description: String, author: User)

    suspend fun delete(articleId: String)
}
