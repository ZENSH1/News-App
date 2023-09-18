package com.xs.firenotesapp.data.users

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersDatabase: UsersDatabase
) : UsersRepository {

    override suspend fun addUser(usersData: UsersData): Flow<FirebaseResults<String>> = usersDatabase.addUser(usersData)

    override fun getUser(userID:String): Flow<FirebaseResults<DocumentSnapshot>> = usersDatabase.getUser(userID)

    override fun getUsers(): Flow<FirebaseResults<List<UsersData>>> = usersDatabase.getUsers()

    override suspend fun editUserData(usersData: UsersData): Flow<FirebaseResults<String>> = usersDatabase.editUserData(usersData)


}