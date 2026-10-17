package com.xs.newsapp.data.fake

import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

/** Mock flavor: accounts live in [InMemoryBackend]. Behaves like the real backend, errors included. */
class FakeAuthRepository @Inject constructor(
    private val backend: InMemoryBackend,
) : AuthRepository {

    override val session: Flow<Session> = backend.session

    override suspend fun signIn(email: String, password: String) {
        delay(NETWORK_LATENCY_MILLIS)
        val account = backend.accounts.value.firstOrNull { it.user.email.equals(email, ignoreCase = true) }
        if (account == null || account.password != password) {
            throw BackendException(BackendError.InvalidCredentials)
        }
        backend.session.value = Session.SignedIn(account.user)
    }

    override suspend fun signUp(name: String, email: String, password: String) {
        delay(NETWORK_LATENCY_MILLIS)
        if (password.length < Validation.MIN_PASSWORD_LENGTH) throw BackendException(BackendError.WeakPassword)
        if (backend.accounts.value.any { it.user.email.equals(email, ignoreCase = true) }) {
            throw BackendException(BackendError.EmailAlreadyInUse)
        }
        val user = User(id = UUID.randomUUID().toString(), name = name, email = email)
        backend.accounts.update { it + InMemoryBackend.Account(user, password) }
        backend.profiles.update { it + (user.id to UserProfile.fallback(user)) }
        backend.session.value = Session.SignedIn(user)
    }

    override suspend fun signOut() {
        backend.session.value = Session.SignedOut
    }
}

internal const val NETWORK_LATENCY_MILLIS = 600L
