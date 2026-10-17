package com.xs.newsapp.feature.editprofile

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.UserProfile

data class EditProfileState(
    val isLoading: Boolean = true,
    val email: String = "",
    val name: String = "",
    /** Raw text; parsed on save. */
    val age: String = "",
    val isSaving: Boolean = false,
    val nameError: FieldError? = null,
    val ageError: FieldError? = null,
)

sealed interface EditProfileIntent {
    data class NameChanged(val name: String) : EditProfileIntent
    data class AgeChanged(val age: String) : EditProfileIntent
    data object Save : EditProfileIntent
    data object BackClicked : EditProfileIntent
}

sealed interface EditProfileMutation {
    data class Loaded(val profile: UserProfile) : EditProfileMutation
    data class NameUpdated(val name: String) : EditProfileMutation
    data class AgeUpdated(val age: String) : EditProfileMutation
    data class ValidationFailed(val nameError: FieldError?, val ageError: FieldError?) : EditProfileMutation
    data object SaveStarted : EditProfileMutation
    data object SaveFinished : EditProfileMutation
}

sealed interface EditProfileEffect {
    data object NavigateBack : EditProfileEffect
    data class ShowError(val error: BackendError) : EditProfileEffect
}

object EditProfileReducer : Reducer<EditProfileState, EditProfileMutation> {

    private const val MAX_AGE_DIGITS = 3

    override fun reduce(state: EditProfileState, mutation: EditProfileMutation): EditProfileState = when (mutation) {
        is EditProfileMutation.Loaded -> state.copy(
            isLoading = false,
            email = mutation.profile.email,
            name = mutation.profile.name,
            age = mutation.profile.age?.toString().orEmpty(),
        )
        is EditProfileMutation.NameUpdated -> state.copy(name = mutation.name, nameError = null)
        is EditProfileMutation.AgeUpdated -> state.copy(
            age = mutation.age.filter(Char::isDigit).take(MAX_AGE_DIGITS),
            ageError = null,
        )
        is EditProfileMutation.ValidationFailed -> state.copy(nameError = mutation.nameError, ageError = mutation.ageError)
        EditProfileMutation.SaveStarted -> state.copy(isSaving = true)
        EditProfileMutation.SaveFinished -> state.copy(isSaving = false)
    }
}
