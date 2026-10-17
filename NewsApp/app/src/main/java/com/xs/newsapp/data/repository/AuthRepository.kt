package com.xs.newsapp.data.repository

import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import kotlinx.coroutines.flow.Flow

/**
 * Sign-in state and account operations.
 * Implemented by an in-memory fake (`mock` flavor) and by Firebase Authentication (`prod` flavor).
 * Suspend functions are main-safe and only throw [BackendException].
 */
interface AuthRepository {

    /** The current [Session], emitted immediately and again on every sign-in or sign-out. */
    val session: Flow<Session>

    suspend fun signIn(email: String, password: String)

    /** Creates the account and its profile, then signs in. */
    suspend fun signUp(name: String, email: String, password: String)

    suspend fun signOut()
}
