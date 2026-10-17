package com.xs.newsapp.data.model

/** Public profile of a user, stored separately from their login. */
data class UserProfile(
    val userId: String,
    val name: String,
    val email: String,
    /** Shown as "Account type", e.g. "Reporter". */
    val role: String,
    val age: Int?,
) {
    companion object {
        const val DEFAULT_ROLE = "Reporter"

        /** A profile built from the login alone, for accounts that have no profile document yet. */
        fun fallback(user: User) = UserProfile(user.id, user.name, user.email, DEFAULT_ROLE, age = null)
    }
}
