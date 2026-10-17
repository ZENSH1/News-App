package com.xs.newsapp.data.fake

import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeProfileRepository @Inject constructor(
    private val backend: InMemoryBackend,
) : ProfileRepository {

    override fun observeProfile(userId: String): Flow<UserProfile?> = backend.profiles.map { it[userId] }

    override suspend fun updateProfile(userId: String, name: String, age: Int?) {
        delay(NETWORK_LATENCY_MILLIS)
        val profile = backend.profiles.value[userId] ?: throw BackendException(BackendError.NotFound)
        backend.profiles.update { it + (userId to profile.copy(name = name, age = age)) }
        // Sync the display name, like Firebase does.
        backend.accounts.update { accounts ->
            accounts.map { if (it.user.id == userId) it.copy(user = it.user.copy(name = name)) else it }
        }
        backend.session.update { session ->
            if (session is Session.SignedIn && session.user.id == userId) {
                Session.SignedIn(session.user.copy(name = name))
            } else {
                session
            }
        }
    }
}
