package com.xs.newsapp.data.model

data class Article(
    val id: String,
    val headline: String,
    val description: String,
    val authorId: String,
    val authorName: String,
    /** Epoch millis; `0` for old stories without one. */
    val publishedAtEpochMillis: Long,
)
