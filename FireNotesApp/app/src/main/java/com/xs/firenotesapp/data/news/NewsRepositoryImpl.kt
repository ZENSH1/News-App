package com.xs.firenotesapp.data.news

import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsDB : NewsDatabase) : NewsRepository {

    override suspend fun postNews(news: NewsData): Flow<FirebaseResults<String>> =
        newsDB.postNews(news)

    override fun getNews(): Flow<FirebaseResults<List<NewsData>>> = newsDB.getNews()

    override suspend fun deleteNews(news: NewsData): Flow<FirebaseResults<String>> =
        newsDB.deleteNews(news)

    override suspend fun editNews(news: NewsData): Flow<FirebaseResults<String>> =
        newsDB.editNews(news)
}
