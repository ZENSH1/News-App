package com.xs.newsapp.feature.editprofile

import app.cash.turbine.test
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestAuthRepository
import com.xs.newsapp.testing.TestProfileRepository
import com.xs.newsapp.testing.TestProfileRepository.Update
import com.xs.newsapp.testing.ayesha
import com.xs.newsapp.testing.profileOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = TestAuthRepository(Session.SignedIn(ayesha))
    private val profileRepository = TestProfileRepository(profileOf(ayesha, age = 30))

    private fun createViewModel() = EditProfileViewModel(authRepository, profileRepository)

    @Test
    fun formStartsWithTheCurrentProfile() = runTest {
        assertEquals(
            EditProfileState(isLoading = false, email = ayesha.email, name = ayesha.name, age = "30"),
            createViewModel().state.value,
        )
    }

    @Test
    fun ageField_keepsAtMostThreeDigits() {
        val next = EditProfileReducer.reduce(EditProfileState(), EditProfileMutation.AgeUpdated("1a2b3c4"))

        assertEquals("123", next.age)
    }

    @Test
    fun invalidInput_isNotSaved() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(EditProfileIntent.NameChanged("  "))
        viewModel.onIntent(EditProfileIntent.AgeChanged("150"))

        viewModel.onIntent(EditProfileIntent.Save)

        assertEquals(FieldError.Blank, viewModel.state.value.nameError)
        assertEquals(FieldError.InvalidAge, viewModel.state.value.ageError)
        assertTrue(profileRepository.updates.isEmpty())
    }

    @Test
    fun saving_updatesTheProfile_andGoesBack() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(EditProfileIntent.NameChanged(" Ayesha K. "))
        viewModel.onIntent(EditProfileIntent.AgeChanged(""))

        viewModel.effects.test {
            viewModel.onIntent(EditProfileIntent.Save)
            assertEquals(EditProfileEffect.NavigateBack, awaitItem())
        }

        assertEquals(listOf(Update(ayesha.id, "Ayesha K.", age = null)), profileRepository.updates)
    }

    @Test
    fun failedSave_staysOnTheForm() = runTest {
        profileRepository.failure = BackendError.Network
        val viewModel = createViewModel()

        viewModel.effects.test {
            viewModel.onIntent(EditProfileIntent.Save)
            assertEquals(EditProfileEffect.ShowError(BackendError.Network), awaitItem())
        }

        assertFalse(viewModel.state.value.isSaving)
        assertEquals(ayesha.name, viewModel.state.value.name)
    }

    @Test
    fun back_navigatesBack() = runTest {
        val viewModel = createViewModel()

        viewModel.effects.test {
            viewModel.onIntent(EditProfileIntent.BackClicked)
            assertEquals(EditProfileEffect.NavigateBack, awaitItem())
        }
    }
}
