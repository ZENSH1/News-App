package com.xs.newsapp.data.model

data class UserProfile(
    val userId: String,
    val name: String,
    val email: String,
    val role: String,
    val age: Int?,
) {
    companion object {
        const val DEFAULT_ROLE = "Reporter"

        /** For accounts without a profile document. */
        fun fallback(user: User) = UserProfile(user.id, user.name, user.email, DEFAULT_ROLE, age = null)
    }
}
