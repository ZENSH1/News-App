package com.xs.newsapp.data.fake

import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** The mock flavor's in-memory "server", seeded with demo data. Login: demo@newsapp.test / password */
@Singleton
class InMemoryBackend @Inject constructor() {

    data class Account(val user: User, val password: String)

    val session = MutableStateFlow<Session>(Session.SignedOut)

    val accounts = MutableStateFlow(
        listOf(
            Account(DEMO_USER, DEMO_PASSWORD),
            Account(User(id = "ayesha", name = "Ayesha Khan", email = "ayesha@newsapp.test"), "password"),
            Account(User(id = "bilal", name = "Bilal Ahmed", email = "bilal@newsapp.test"), "password"),
        ),
    )

    val profiles = MutableStateFlow(
        mapOf(
            DEMO_USER.id to UserProfile(DEMO_USER.id, DEMO_USER.name, DEMO_USER.email, "Reporter", age = 27),
            "ayesha" to UserProfile("ayesha", "Ayesha Khan", "ayesha@newsapp.test", "Editor", age = 31),
            "bilal" to UserProfile("bilal", "Bilal Ahmed", "bilal@newsapp.test", "Reporter", age = null),
        ),
    )

    val articles = MutableStateFlow(seedArticles(System.currentTimeMillis()))

    companion object {
        val DEMO_USER = User(id = "demo", name = "Demo Reporter", email = "demo@newsapp.test")
        const val DEMO_PASSWORD = "password"

        private const val MINUTE = 60_000L
        private const val HOUR = 60 * MINUTE

        private fun seedArticles(now: Long) = listOf(
            Article(
                id = "a1",
                headline = "City council approves new cycling lanes",
                description = "Twelve kilometres of protected lanes will connect the university to the old town by next spring.",
                authorId = "ayesha",
                authorName = "Ayesha Khan",
                publishedAtEpochMillis = now - 20 * MINUTE,
            ),
            Article(
                id = "a2",
                headline = "Local team reaches the national final",
                description = "A late goal sealed a 2-1 win in front of a sold-out crowd on Saturday night.",
                authorId = "demo",
                authorName = "Demo Reporter",
                publishedAtEpochMillis = now - 2 * HOUR,
            ),
            Article(
                id = "a3",
                headline = "Heatwave expected to break this weekend",
                description = "Forecasters predict cooler winds and scattered showers from Sunday afternoon.",
                authorId = "bilal",
                authorName = "Bilal Ahmed",
                publishedAtEpochMillis = now - 5 * HOUR,
            ),
            Article(
                id = "a4",
                headline = "Library opens a 24-hour study hall",
                description = "Students can book seats online; the hall has quiet zones and free Wi-Fi.",
                authorId = "demo",
                authorName = "Demo Reporter",
                publishedAtEpochMillis = now - 26 * HOUR,
            ),
            Article(
                id = "a5",
                headline = "Food festival returns to the waterfront",
                description = "More than eighty stalls are taking part this year, with a focus on regional dishes.",
                authorId = "ayesha",
                authorName = "Ayesha Khan",
                publishedAtEpochMillis = now - 50 * HOUR,
            ),
        )
    }
}
