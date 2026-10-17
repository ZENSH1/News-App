package com.xs.newsapp.data.model

/** Why a backend call failed, in terms the UI can explain to the user. */
enum class BackendError {
    InvalidCredentials,
    EmailAlreadyInUse,
    WeakPassword,
    Network,
    PermissionDenied,
    NotFound,
    Unknown,
}

/** The only exception repositories throw. Stores catch it and turn [error] into an effect or state. */
class BackendException(
    val error: BackendError,
    cause: Throwable? = null,
) : Exception("Backend failure: $error", cause)
