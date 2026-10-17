package com.xs.newsapp.data.model

/** The signed-in account. */
data class User(
    val id: String,
    val name: String,
    val email: String,
)

/** Whether someone is signed in. The whole app's navigation is derived from this. */
sealed interface Session {

    /** The backend hasn't told us yet (e.g. still restoring a saved login). */
    data object Unknown : Session

    data object SignedOut : Session

    data class SignedIn(val user: User) : Session
}
