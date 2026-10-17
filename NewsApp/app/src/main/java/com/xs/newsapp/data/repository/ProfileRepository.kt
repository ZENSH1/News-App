package com.xs.newsapp.data.repository

import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

/** Main-safe; throws only [BackendException]. */
interface ProfileRepository {

    fun observeProfile(userId: String): Flow<UserProfile?>

    suspend fun updateProfile(userId: String, name: String, age: Int?)
}
