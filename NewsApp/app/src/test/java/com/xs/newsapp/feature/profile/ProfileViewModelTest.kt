package com.xs.newsapp.feature.profile

import app.cash.turbine.test
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.data.model.User
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestAuthRepository
import com.xs.newsapp.testing.TestNewsRepository
import com.xs.newsapp.testing.TestProfileRepository
import com.xs.newsapp.testing.article
import com.xs.newsapp.testing.ayesha
import com.xs.newsapp.testing.bilal
import com.xs.newsapp.testing.profileOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = TestAuthRepository(Session.SignedIn(ayesha))
    private val profileRepository = TestProfileRepository(profileOf(ayesha, age = 30), profileOf(bilal))
    private val newsRepository = TestNewsRepository(article("a1", ayesha), article("b1", bilal), article("a2", ayesha))

    private fun createViewModel() = ProfileViewModel(authRepository, profileRepository, newsRepository)

    @Test
    fun showsTheProfile_andOnlyTheUsersOwnStories() = runTest {
        val state = createViewModel().state.value

        assertFalse(state.isLoading)
        assertEquals(profileOf(ayesha, age = 30), state.profile)
        assertEquals(listOf("a1", "a2"), state.articles.map { it.id })
    }

    @Test
    fun accountWithoutAProfile_fallsBackToItsLoginDetails() = runTest {
        authRepository.setSession(Session.SignedIn(User("new", "New Reporter", "new@newsapp.test")))

        val state = createViewModel().state.value

        assertEquals(UserProfile("new", "New Reporter", "new@newsapp.test", UserProfile.DEFAULT_ROLE, age = null), state.profile)
        assertTrue(state.articles.isEmpty())
    }

    @Test
    fun switchingAccounts_neverShowsThePreviousAccountsData() = runTest {
        val viewModel = createViewModel()

        authRepository.setSession(Session.SignedIn(bilal))

        assertEquals(profileOf(bilal), viewModel.state.value.profile)
        assertEquals(listOf("b1"), viewModel.state.value.articles.map { it.id })
    }

    @Test
    fun loadingFailure_showsTheError() = runTest {
        profileRepository.observeFailure = BackendError.PermissionDenied

        val state = createViewModel().state.value

        assertFalse(state.isLoading)
        assertNull(state.profile)
        assertEquals(BackendError.PermissionDenied, state.error)
    }

    @Test
    fun deleting_asksForConfirmationFirst() = runTest {
        val viewModel = createViewModel()
        val story = article("a1", ayesha)

        viewModel.onIntent(ProfileIntent.DeleteArticleClicked(story))
        assertEquals(story, viewModel.state.value.articlePendingDeletion)

        viewModel.onIntent(ProfileIntent.DeleteDismissed)
        assertNull(viewModel.state.value.articlePendingDeletion)
        assertTrue(newsRepository.deletedIds.isEmpty())
    }

    @Test
    fun confirmingTheDeletion_deletesTheStory() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(ProfileIntent.DeleteArticleClicked(article("a1", ayesha)))

        viewModel.effects.test {
            viewModel.onIntent(ProfileIntent.DeleteConfirmed)
            assertEquals(ProfileEffect.ArticleDeleted, awaitItem())
        }

        assertEquals(listOf("a1"), newsRepository.deletedIds)
        val state = viewModel.state.value
        assertEquals(listOf("a2"), state.articles.map { it.id })
        assertNull(state.articlePendingDeletion)
        assertFalse(state.isDeleting)
    }

    @Test
    fun failedDeletion_isReported_andTheStoryStays() = runTest {
        newsRepository.failure = BackendError.PermissionDenied
        val viewModel = createViewModel()
        viewModel.onIntent(ProfileIntent.DeleteArticleClicked(article("a1", ayesha)))

        viewModel.effects.test {
            viewModel.onIntent(ProfileIntent.DeleteConfirmed)
            assertEquals(ProfileEffect.ShowError(BackendError.PermissionDenied), awaitItem())
        }

        assertEquals(listOf("a1", "a2"), viewModel.state.value.articles.map { it.id })
        assertFalse(viewModel.state.value.isDeleting)
    }

    @Test
    fun signOut_endsTheSession() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(ProfileIntent.SignOutClicked)

        assertEquals(1, authRepository.signOutCalls)
    }

    @Test
    fun editProfile_navigatesToTheEditor() = runTest {
        val viewModel = createViewModel()

        viewModel.effects.test {
            viewModel.onIntent(ProfileIntent.EditProfileClicked)
            assertEquals(ProfileEffect.NavigateToEditProfile, awaitItem())
        }
    }
}
