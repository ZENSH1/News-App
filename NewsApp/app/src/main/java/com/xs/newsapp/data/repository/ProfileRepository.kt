package com.xs.newsapp.data.repository

import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

/** User profiles. Suspend functions are main-safe and only throw [BackendException]. */
interface ProfileRepository {

    /** The profile of [userId], or `null` if it has none, updated in real time. */
    fun observeProfile(userId: String): Flow<UserProfile?>

    suspend fun updateProfile(userId: String, name: String, age: Int?)
}
