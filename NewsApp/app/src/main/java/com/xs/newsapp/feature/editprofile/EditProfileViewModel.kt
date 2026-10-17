package com.xs.newsapp.feature.editprofile

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.data.repository.AuthRepository
import com.xs.newsapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : MviViewModel<EditProfileState, EditProfileIntent, EditProfileMutation, EditProfileEffect>(
    EditProfileState(),
    EditProfileReducer,
) {

    private var user: User? = null

    init {
        // Load once so live updates don't overwrite typing.
        viewModelScope.launch {
            val signedInUser = authRepository.session.filterIsInstance<Session.SignedIn>().first().user
            user = signedInUser
            val profile = try {
                profileRepository.observeProfile(signedInUser.id).first()
            } catch (e: BackendException) {
                sendEffect(EditProfileEffect.ShowError(e.error))
                null
            }
            mutate(EditProfileMutation.Loaded(profile ?: UserProfile.fallback(signedInUser)))
        }
    }

    override fun handleIntent(intent: EditProfileIntent) {
        when (intent) {
            is EditProfileIntent.NameChanged -> mutate(EditProfileMutation.NameUpdated(intent.name))
            is EditProfileIntent.AgeChanged -> mutate(EditProfileMutation.AgeUpdated(intent.age))
            EditProfileIntent.BackClicked -> sendEffect(EditProfileEffect.NavigateBack)
            EditProfileIntent.Save -> save()
        }
    }

    private fun save() {
        val state = currentState
        val user = user ?: return
        if (state.isLoading || state.isSaving) return

        val nameError = Validation.required(state.name)
        val ageError = Validation.optionalAge(state.age)
        if (nameError != null || ageError != null) {
            mutate(EditProfileMutation.ValidationFailed(nameError, ageError))
            return
        }

        mutate(EditProfileMutation.SaveStarted)
        viewModelScope.launch {
            try {
                profileRepository.updateProfile(user.id, state.name.trim(), state.age.toIntOrNull())
                sendEffect(EditProfileEffect.NavigateBack)
            } catch (e: BackendException) {
                sendEffect(EditProfileEffect.ShowError(e.error))
            } finally {
                mutate(EditProfileMutation.SaveFinished)
            }
        }
    }
}
