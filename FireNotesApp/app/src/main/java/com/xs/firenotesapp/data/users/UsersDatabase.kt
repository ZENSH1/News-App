package com.xs.firenotesapp.data.users

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UsersDatabase @Inject constructor(
    firestore: FirebaseFirestore,
    private val currentUser: FirebaseUser?
) {
    private val userReference = firestore.collection("Users")
    fun getUsers(): Flow<FirebaseResults<List<UsersData>>> = callbackFlow {
        trySend(FirebaseResults.Loading)
        val listener = userReference.addSnapshotListener { value, error ->
            if (error != null) trySend(FirebaseResults.Failure(error))
            if (value != null) {
                val usersData = value.map { snapshot ->
                    snapshot.toObject<UsersData>()
                }
                trySend(FirebaseResults.Success(usersData))
            }

        }
        awaitClose { listener.remove() }
    }

    fun getUser(userID:String): Flow<FirebaseResults<DocumentSnapshot>> = callbackFlow {
        trySend(FirebaseResults.Loading)
        val listener = userReference.document(userID).addSnapshotListener{
            value, error ->
            if (error!= null) trySend(FirebaseResults.Failure(error))
            if (value !=null){
                val currentdata = value
                trySend(FirebaseResults.Success(currentdata))
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun addUser(usersData: UsersData): Flow<FirebaseResults<String>> = callbackFlow {
        val userID = currentUser?.uid
        userReference.document(userID!!).set(usersData)
            .addOnSuccessListener { trySend(FirebaseResults.Success("Account Created Successfully")) }
            .addOnFailureListener {FirebaseResults.Failure(it) }
        awaitClose { close() }
    }

    suspend fun editUserData(usersData: UsersData):Flow<FirebaseResults<String>> = callbackFlow {
        val userID = usersData.id
        userReference.document(userID).set(usersData)
            .addOnSuccessListener { trySend(FirebaseResults.Success("Data Updated")) }
            .addOnFailureListener {FirebaseResults.Failure(it) }
        awaitClose { close() }
    }


}