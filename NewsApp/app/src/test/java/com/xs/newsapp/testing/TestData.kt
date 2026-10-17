package com.xs.newsapp.testing

import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile

val ayesha = User(id = "ayesha", name = "Ayesha Khan", email = "ayesha@newsapp.test")
val bilal = User(id = "bilal", name = "Bilal Ahmed", email = "bilal@newsapp.test")

fun profileOf(user: User, age: Int? = null) = UserProfile(
    userId = user.id,
    name = user.name,
    email = user.email,
    role = UserProfile.DEFAULT_ROLE,
    age = age,
)

fun article(id: String, author: User = ayesha) = Article(
    id = id,
    headline = "Headline $id",
    description = "Story $id",
    authorId = author.id,
    authorName = author.name,
    publishedAtEpochMillis = 0,
)
