package com.xs.newsapp.testing

import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.AuthRepository
import com.xs.newsapp.data.repository.NewsRepository
import com.xs.newsapp.data.repository.ProfileRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

// Fakes that record calls and can fail or hold a call open (gate).

abstract class ControllableFake {
    var failure: BackendError? = null

    /** Holds calls open until completed. */
    var gate: CompletableDeferred<Unit>? = null

    protected suspend fun respond() {
        gate?.await()
        failure?.let { throw BackendException(it) }
    }
}

class TestAuthRepository(initialSession: Session = Session.SignedOut) : ControllableFake(), AuthRepository {

    private val _session = MutableStateFlow(initialSession)
    override val session: Flow<Session> = _session

    val signInCalls = mutableListOf<Pair<String, String>>()
    val signUpCalls = mutableListOf<Triple<String, String, String>>()
    var signOutCalls = 0
        private set

    fun setSession(session: Session) {
        _session.value = session
    }

    override suspend fun signIn(email: String, password: String) {
        signInCalls += email to password
        respond()
        _session.value = Session.SignedIn(User(id = email, name = email.substringBefore('@'), email = email))
    }

    override suspend fun signUp(name: String, email: String, password: String) {
        signUpCalls += Triple(name, email, password)
        respond()
        _session.value = Session.SignedIn(User(id = email, name = name, email = email))
    }

    override suspend fun signOut() {
        signOutCalls++
        respond()
        _session.value = Session.SignedOut
    }
}

class TestNewsRepository(vararg articles: Article) : ControllableFake(), NewsRepository {

    data class Publication(val headline: String, val description: String, val author: User)

    private val articles = MutableStateFlow(articles.toList())

    var observeFailure: BackendError? = null

    val publications = mutableListOf<Publication>()
    val deletedIds = mutableListOf<String>()

    fun setArticles(vararg list: Article) {
        articles.value = list.toList()
    }

    override fun observeArticles(): Flow<List<Article>> = flow {
        observeFailure?.let { throw BackendException(it) }
        emitAll(articles)
    }

    override suspend fun publish(headline: String, description: String, author: User) {
        publications += Publication(headline, description, author)
        respond()
        val article = Article("new-${publications.size}", headline, description, author.id, author.name, 0)
        articles.update { listOf(article) + it }
    }

    override suspend fun delete(articleId: String) {
        deletedIds += articleId
        respond()
        articles.update { list -> list.filterNot { it.id == articleId } }
    }
}

class TestProfileRepository(vararg profiles: UserProfile) : ControllableFake(), ProfileRepository {

    data class Update(val userId: String, val name: String, val age: Int?)

    private val profiles = MutableStateFlow(profiles.associateBy { it.userId })

    var observeFailure: BackendError? = null

    val updates = mutableListOf<Update>()

    override fun observeProfile(userId: String): Flow<UserProfile?> = flow {
        observeFailure?.let { throw BackendException(it) }
        emitAll(profiles.map { it[userId] }.distinctUntilChanged())
    }

    override suspend fun updateProfile(userId: String, name: String, age: Int?) {
        updates += Update(userId, name, age)
        respond()
        profiles.update { all ->
            val current = all[userId] ?: UserProfile(userId, name, "", UserProfile.DEFAULT_ROLE, age)
            all + (userId to current.copy(name = name, age = age))
        }
    }
}
