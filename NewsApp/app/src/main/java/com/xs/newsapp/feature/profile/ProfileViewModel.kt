package com.xs.newsapp.feature.profile

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.AuthRepository
import com.xs.newsapp.data.repository.NewsRepository
import com.xs.newsapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val newsRepository: NewsRepository,
) : MviViewModel<ProfileState, ProfileIntent, ProfileMutation, ProfileEffect>(ProfileState(), ProfileReducer) {

    init {
        viewModelScope.launch {
            loadedProfiles()
                .catch { error ->
                    if (error is BackendException) emit(ProfileMutation.LoadingFailed(error.error)) else throw error
                }
                .collect { mutation -> mutate(mutation) }
        }
    }

    override fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.EditProfileClicked -> sendEffect(ProfileEffect.NavigateToEditProfile)
            is ProfileIntent.DeleteArticleClicked -> mutate(ProfileMutation.DeletionRequested(intent.article))
            ProfileIntent.DeleteDismissed -> mutate(ProfileMutation.DeletionDismissed)
            ProfileIntent.DeleteConfirmed -> deletePendingArticle()
            ProfileIntent.SignOutClicked -> signOut()
        }
    }

    /**
     * The profile and the user's own stories, re-derived whenever the signed-in user changes,
     * so this store never shows stale data from a previous account.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadedProfiles(): Flow<ProfileMutation> = authRepository.session
        .filterIsInstance<Session.SignedIn>()
        .flatMapLatest { session ->
            val user = session.user
            combine(
                profileRepository.observeProfile(user.id),
                newsRepository.observeArticles(),
            ) { profile, articles ->
                ProfileMutation.Loaded(
                    profile = profile ?: UserProfile.fallback(user),
                    articles = articles.filter { it.authorId == user.id },
                )
            }
        }

    private fun deletePendingArticle() {
        val article = currentState.articlePendingDeletion ?: return
        mutate(ProfileMutation.DeletionStarted)
        viewModelScope.launch {
            try {
                newsRepository.delete(article.id)
                sendEffect(ProfileEffect.ArticleDeleted)
            } catch (e: BackendException) {
                sendEffect(ProfileEffect.ShowError(e.error))
            } finally {
                mutate(ProfileMutation.DeletionFinished)
            }
        }
    }

    private fun signOut() {
        if (currentState.isSigningOut) return
        mutate(ProfileMutation.SignOutStarted)
        viewModelScope.launch {
            try {
                // The session becomes SignedOut and the app shell shows the login screen.
                authRepository.signOut()
            } catch (e: BackendException) {
                mutate(ProfileMutation.SignOutFailed)
                sendEffect(ProfileEffect.ShowError(e.error))
            }
        }
    }
}
