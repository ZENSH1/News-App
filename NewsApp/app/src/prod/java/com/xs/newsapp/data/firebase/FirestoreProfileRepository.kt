package com.xs.newsapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.ProfileRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** `prod` flavor: the "Users" collection in Cloud Firestore, one document per user id. */
class FirestoreProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ProfileRepository {

    override fun observeProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val registration = firestore.collection(FirestoreCollections.USERS).document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(BackendException(error.toBackendError(), error))
                    return@addSnapshotListener
                }
                trySend(snapshot?.takeIf { it.exists() }?.toProfile())
            }
        awaitClose { registration.remove() }
    }

    override suspend fun updateProfile(userId: String, name: String, age: Int?) {
        firebaseCall {
            firestore.collection(FirestoreCollections.USERS).document(userId)
                .set(mapOf("name" to name, "age" to (age?.toString() ?: "")), SetOptions.merge())
                .await()
            // Keep the login's display name (used for new bylines) in sync.
            val currentUser = auth.currentUser
            if (currentUser != null && currentUser.uid == userId) {
                currentUser.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
            }
        }
    }
}

private fun DocumentSnapshot.toProfile() = UserProfile(
    userId = id,
    name = stringField("name").orEmpty(),
    email = stringField("email").orEmpty(),
    role = stringField("userRole")?.takeIf { it.isNotBlank() } ?: UserProfile.DEFAULT_ROLE,
    age = longField("age")?.toInt(),
)
