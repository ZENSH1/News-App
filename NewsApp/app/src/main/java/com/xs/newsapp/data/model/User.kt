package com.xs.newsapp.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
)

/** Drives the app's top-level navigation. */
sealed interface Session {

    /** Not known yet, e.g. restoring a saved login. */
    data object Unknown : Session

    data object SignedOut : Session

    data class SignedIn(val user: User) : Session
}
