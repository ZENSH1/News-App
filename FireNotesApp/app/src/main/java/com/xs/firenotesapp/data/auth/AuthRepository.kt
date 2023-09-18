package com.xs.firenotesapp.data.auth

import com.google.firebase.auth.FirebaseUser
import com.xs.firenotesapp.utils.FirebaseResults

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email:String, password:String): FirebaseResults<FirebaseUser>
    suspend fun signUp(name:String, email: String,password: String): FirebaseResults<FirebaseUser>
    fun logout()
}