package com.xs.newsapp.data.model

/** Why a backend call failed. */
enum class BackendError {
    InvalidCredentials,
    EmailAlreadyInUse,
    WeakPassword,
    Network,
    PermissionDenied,
    NotFound,
    Unknown,
}

/** The only exception repositories throw. */
class BackendException(
    val error: BackendError,
    cause: Throwable? = null,
) : Exception("Backend failure: $error", cause)
