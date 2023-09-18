package com.xs.firenotesapp.data.users

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface UsersRepository {

    suspend fun addUser(usersData: UsersData): Flow<FirebaseResults<String>>

    fun getUser(userID:String): Flow<FirebaseResults<DocumentSnapshot>>

    fun getUsers(): Flow<FirebaseResults<List<UsersData>>>

    suspend fun editUserData(usersData: UsersData):Flow<FirebaseResults<String>>

}