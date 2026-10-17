package com.xs.newsapp.feature.publish

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError

data class PublishState(
    val headline: String = "",
    val description: String = "",
    val isPublishing: Boolean = false,
    val headlineError: FieldError? = null,
    val descriptionError: FieldError? = null,
)

sealed interface PublishIntent {
    data class HeadlineChanged(val headline: String) : PublishIntent
    data class DescriptionChanged(val description: String) : PublishIntent
    data object Publish : PublishIntent
}

sealed interface PublishMutation {
    data class HeadlineUpdated(val headline: String) : PublishMutation
    data class DescriptionUpdated(val description: String) : PublishMutation
    data class ValidationFailed(val headlineError: FieldError?, val descriptionError: FieldError?) : PublishMutation
    data object PublishStarted : PublishMutation
    data object PublishSucceeded : PublishMutation
    data object PublishFailed : PublishMutation
}

sealed interface PublishEffect {
    data object Published : PublishEffect
    data class ShowError(val error: BackendError) : PublishEffect
}

object PublishReducer : Reducer<PublishState, PublishMutation> {
    override fun reduce(state: PublishState, mutation: PublishMutation): PublishState = when (mutation) {
        is PublishMutation.HeadlineUpdated -> state.copy(headline = mutation.headline, headlineError = null)
        is PublishMutation.DescriptionUpdated ->
            state.copy(description = mutation.description, descriptionError = null)
        is PublishMutation.ValidationFailed -> state.copy(
            headlineError = mutation.headlineError,
            descriptionError = mutation.descriptionError,
        )
        PublishMutation.PublishStarted -> state.copy(isPublishing = true)
        PublishMutation.PublishSucceeded -> PublishState()
        PublishMutation.PublishFailed -> state.copy(isPublishing = false)
    }
}
