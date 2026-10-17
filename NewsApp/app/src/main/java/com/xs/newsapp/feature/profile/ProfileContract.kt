package com.xs.newsapp.feature.profile

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.UserProfile

data class ProfileState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    /** Only the signed-in user's own stories. */
    val articles: List<Article> = emptyList(),
    val error: BackendError? = null,
    /** Non-null while the "delete this story?" dialog is open. A dialog is state, not an effect. */
    val articlePendingDeletion: Article? = null,
    val isDeleting: Boolean = false,
    val isSigningOut: Boolean = false,
)

sealed interface ProfileIntent {
    data object EditProfileClicked : ProfileIntent
    data object SignOutClicked : ProfileIntent
    data class DeleteArticleClicked(val article: Article) : ProfileIntent
    data object DeleteConfirmed : ProfileIntent
    data object DeleteDismissed : ProfileIntent
}

sealed interface ProfileMutation {
    data class Loaded(val profile: UserProfile, val articles: List<Article>) : ProfileMutation
    data class LoadingFailed(val error: BackendError) : ProfileMutation
    data class DeletionRequested(val article: Article) : ProfileMutation
    data object DeletionDismissed : ProfileMutation
    data object DeletionStarted : ProfileMutation
    data object DeletionFinished : ProfileMutation
    data object SignOutStarted : ProfileMutation
    data object SignOutFailed : ProfileMutation
}

sealed interface ProfileEffect {
    data object NavigateToEditProfile : ProfileEffect
    data object ArticleDeleted : ProfileEffect
    data class ShowError(val error: BackendError) : ProfileEffect
}

object ProfileReducer : Reducer<ProfileState, ProfileMutation> {
    override fun reduce(state: ProfileState, mutation: ProfileMutation): ProfileState = when (mutation) {
        is ProfileMutation.Loaded -> state.copy(
            isLoading = false,
            profile = mutation.profile,
            articles = mutation.articles,
            error = null,
        )
        is ProfileMutation.LoadingFailed -> state.copy(isLoading = false, error = mutation.error)
        is ProfileMutation.DeletionRequested -> state.copy(articlePendingDeletion = mutation.article)
        ProfileMutation.DeletionDismissed -> state.copy(articlePendingDeletion = null)
        ProfileMutation.DeletionStarted -> state.copy(articlePendingDeletion = null, isDeleting = true)
        ProfileMutation.DeletionFinished -> state.copy(isDeleting = false)
        ProfileMutation.SignOutStarted -> state.copy(isSigningOut = true)
        ProfileMutation.SignOutFailed -> state.copy(isSigningOut = false)
    }
}
