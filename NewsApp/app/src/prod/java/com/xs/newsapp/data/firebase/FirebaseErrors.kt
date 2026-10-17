package com.xs.newsapp.data.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import kotlin.coroutines.cancellation.CancellationException

/** Maps Firebase exceptions to [BackendException]. */
internal suspend fun <T> firebaseCall(block: suspend () -> T): T = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: BackendException) {
    throw e
} catch (e: Exception) {
    throw BackendException(e.toBackendError(), e)
}

internal fun Exception.toBackendError(): BackendError = when (this) {
    // Subclass of InvalidCredentials: check first.
    is FirebaseAuthWeakPasswordException -> BackendError.WeakPassword
    is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException -> BackendError.InvalidCredentials
    is FirebaseAuthUserCollisionException -> BackendError.EmailAlreadyInUse
    is FirebaseNetworkException -> BackendError.Network
    is FirebaseFirestoreException -> when (code) {
        FirebaseFirestoreException.Code.PERMISSION_DENIED, FirebaseFirestoreException.Code.UNAUTHENTICATED ->
            BackendError.PermissionDenied
        FirebaseFirestoreException.Code.UNAVAILABLE, FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
            BackendError.Network
        FirebaseFirestoreException.Code.NOT_FOUND -> BackendError.NotFound
        else -> BackendError.Unknown
    }
    else -> BackendError.Unknown
}

/** Tolerates old documents that stored another type. */
internal fun DocumentSnapshot.stringField(field: String): String? = get(field) as? String

internal fun DocumentSnapshot.longField(field: String): Long? = when (val value = get(field)) {
    is Number -> value.toLong()
    is String -> value.toLongOrNull()
    else -> null
}
