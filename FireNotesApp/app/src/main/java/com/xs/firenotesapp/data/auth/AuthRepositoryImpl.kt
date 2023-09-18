package com.xs.firenotesapp.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val mAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = mAuth.currentUser

    override suspend fun login(email: String, password: String): FirebaseResults<FirebaseUser> {
        return try {
            val result = mAuth.signInWithEmailAndPassword(email, password).await()
            FirebaseResults.Success(result.user!!)
        } catch (e: Exception) {
            FirebaseResults.Failure(e)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): FirebaseResults<FirebaseUser> {
        return try {
            val result = mAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(userProfileChangeRequest { this.displayName = name })?.await()
            FirebaseResults.Success(result.user!!)
        } catch (e: Exception) {
            FirebaseResults.Failure(e)
        }
    }

    override fun logout() {
        mAuth.signOut()
    }
}