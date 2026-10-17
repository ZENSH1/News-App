package com.xs.newsapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.AuthRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/** Firebase Auth; new accounts also get a Users document. */
@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepository {

    private val _session = MutableStateFlow(auth.currentUser.toSession())
    override val session: Flow<Session> = _session.asStateFlow()

    init {
        // Singleton, so the listener is never removed.
        auth.addAuthStateListener { firebaseAuth -> _session.value = firebaseAuth.currentUser.toSession() }
    }

    override suspend fun signIn(email: String, password: String) {
        firebaseCall { auth.signInWithEmailAndPassword(email, password).await() }
    }

    override suspend fun signUp(name: String, email: String, password: String) {
        firebaseCall {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
                ?: throw BackendException(BackendError.Unknown)
            // The user is signed in now and the sign-up screen may close; finish setup anyway.
            withContext(NonCancellable) {
                user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
                firestore.collection(FirestoreCollections.USERS).document(user.uid).set(
                    mapOf(
                        "name" to name,
                        "email" to email,
                        "userRole" to UserProfile.DEFAULT_ROLE,
                        "age" to "",
                    ),
                ).await()
                // The listener fired before the name was set.
                _session.value = Session.SignedIn(User(user.uid, name, email))
            }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}

private fun FirebaseUser?.toSession(): Session {
    if (this == null) return Session.SignedOut
    val email = email.orEmpty()
    val name = displayName?.takeIf { it.isNotBlank() } ?: email.substringBefore('@')
    return Session.SignedIn(User(id = uid, name = name, email = email))
}

internal object FirestoreCollections {
    const val USERS = "Users"
    const val NEWS = "News"
}
