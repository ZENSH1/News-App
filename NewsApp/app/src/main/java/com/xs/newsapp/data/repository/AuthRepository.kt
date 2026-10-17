package com.xs.newsapp.data.repository

import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import kotlinx.coroutines.flow.Flow

/** Main-safe; throws only [BackendException]. */
interface AuthRepository {

    val session: Flow<Session>

    suspend fun signIn(email: String, password: String)

    suspend fun signUp(name: String, email: String, password: String)

    suspend fun signOut()
}
