package com.xs.firenotesapp.data.news

import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface NewsRepository {
    fun getNews():Flow<FirebaseResults<List<NewsData>>>

    suspend fun postNews(news: NewsData): Flow<FirebaseResults<String>>

    suspend fun deleteNews(news: NewsData): Flow<FirebaseResults<String>>

    suspend fun editNews(news: NewsData): Flow<FirebaseResults<String>>



}