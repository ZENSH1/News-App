package com.xs.firenotesapp.data.news

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NewsDatabase @Inject constructor(private val newsReference: CollectionReference) {

    fun getNews(): Flow<FirebaseResults<List<NewsData>>> = callbackFlow {
        trySend(FirebaseResults.Loading)
        val listener = newsReference.addSnapshotListener { value, error ->
            if (error != null) trySend(FirebaseResults.Failure(error))
            if (value != null) {
                val newsData = value.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject<NewsData>()
                }
                trySend(FirebaseResults.Success(newsData))
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun editNews(newsData: NewsData): Flow<FirebaseResults<String>> = callbackFlow {
        newsReference.document(newsData.id).update(newsData.toMap())
            .addOnSuccessListener { FirebaseResults.Success("News Posted") }
            .addOnFailureListener { FirebaseResults.Failure(it) }
        awaitClose { close() }
    }

    suspend fun postNews(newsData: NewsData): Flow<FirebaseResults<String>> = callbackFlow {
        val documentID = newsReference.document().id
        newsReference.document(documentID).set(newsData.copy(id = documentID))
            .addOnSuccessListener { trySend(FirebaseResults.Success("User Added")) }
            .addOnFailureListener { trySend(FirebaseResults.Failure(it)) }
        awaitClose { close() }
    }

    suspend fun deleteNews(fireStoreNewsData: NewsData): Flow<FirebaseResults<String>> =
        callbackFlow {
            newsReference.document(fireStoreNewsData.id).delete()
                .addOnSuccessListener { trySend(FirebaseResults.Success("News Deleted")) }
                .addOnFailureListener { trySend(FirebaseResults.Failure(it)) }
            awaitClose { close() }
        }

}