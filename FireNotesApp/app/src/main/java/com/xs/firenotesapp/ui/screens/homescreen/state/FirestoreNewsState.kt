package com.xs.firenotesapp.ui.screens.homescreen.state

import com.xs.firenotesapp.data.news.NewsData

data class FirestoreNewsState(
    val data: List<NewsData?>? = null,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)