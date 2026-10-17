package com.xs.newsapp.data.model

/** A news story. */
data class Article(
    val id: String,
    val headline: String,
    val description: String,
    val authorId: String,
    val authorName: String,
    /** When it was posted, in epoch milliseconds. `0` for old stories saved without a timestamp. */
    val publishedAtEpochMillis: Long,
)
