package com.xs.newsapp.feature.publish

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.repository.AuthRepository
import com.xs.newsapp.data.repository.NewsRepository
import com.xs.newsapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val newsRepository: NewsRepository,
) : MviViewModel<PublishState, PublishIntent, PublishMutation, PublishEffect>(PublishState(), PublishReducer) {

    override fun handleIntent(intent: PublishIntent) {
        when (intent) {
            is PublishIntent.HeadlineChanged -> mutate(PublishMutation.HeadlineUpdated(intent.headline))
            is PublishIntent.DescriptionChanged -> mutate(PublishMutation.DescriptionUpdated(intent.description))
            PublishIntent.Publish -> publish()
        }
    }

    private fun publish() {
        val state = currentState
        if (state.isPublishing) return

        val headlineError = Validation.maxLength(state.headline, Validation.MAX_HEADLINE_LENGTH)
        val descriptionError = Validation.maxLength(state.description, Validation.MAX_DESCRIPTION_LENGTH)
        if (headlineError != null || descriptionError != null) {
            mutate(PublishMutation.ValidationFailed(headlineError, descriptionError))
            return
        }

        mutate(PublishMutation.PublishStarted)
        viewModelScope.launch {
            try {
                val user = (authRepository.session.first() as? Session.SignedIn)?.user
                    ?: throw BackendException(BackendError.PermissionDenied)
                // Byline: the editable profile name, if any.
                val profileName = profileRepository.observeProfile(user.id).first()?.name
                val author = if (profileName.isNullOrBlank()) user else user.copy(name = profileName)
                newsRepository.publish(state.headline.trim(), state.description.trim(), author)
                mutate(PublishMutation.PublishSucceeded)
                sendEffect(PublishEffect.Published)
            } catch (e: BackendException) {
                mutate(PublishMutation.PublishFailed)
                sendEffect(PublishEffect.ShowError(e.error))
            }
        }
    }
}
