package com.xs.newsapp.data.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Firestore "News". Field names match older app versions; stories without `publishedAt` sort last. */
class FirestoreNewsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : NewsRepository {

    private val news get() = firestore.collection(FirestoreCollections.NEWS)

    override fun observeArticles(): Flow<List<Article>> = callbackFlow {
        val registration = news.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(BackendException(error.toBackendError(), error))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val articles = snapshot.documents
                    .mapNotNull { it.toArticle() }
                    .sortedByDescending { it.publishedAtEpochMillis }
                trySend(articles)
            }
        }
        awaitClose { registration.remove() }
    }

    override suspend fun publish(headline: String, description: String, author: User) {
        firebaseCall {
            val document = news.document()
            document.set(
                mapOf(
                    "id" to document.id,
                    "headline" to headline,
                    "description" to description,
                    "author" to author.name,
                    "userID" to author.id,
                    "publishedAt" to System.currentTimeMillis(),
                ),
            ).await()
        }
    }

    override suspend fun delete(articleId: String) {
        firebaseCall { news.document(articleId).delete().await() }
    }
}

private fun DocumentSnapshot.toArticle(): Article? {
    val headline = stringField("headline") ?: return null
    return Article(
        id = id,
        headline = headline,
        description = stringField("description").orEmpty(),
        authorId = stringField("userID").orEmpty(),
        authorName = stringField("author").orEmpty(),
        publishedAtEpochMillis = longField("publishedAt") ?: 0L,
    )
}
