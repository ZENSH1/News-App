package com.xs.newsapp.feature.publish

import app.cash.turbine.test
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.data.model.Session
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestAuthRepository
import com.xs.newsapp.testing.TestNewsRepository
import com.xs.newsapp.testing.TestNewsRepository.Publication
import com.xs.newsapp.testing.TestProfileRepository
import com.xs.newsapp.testing.ayesha
import com.xs.newsapp.testing.profileOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PublishViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = TestAuthRepository(Session.SignedIn(ayesha))
    private val profileRepository = TestProfileRepository(profileOf(ayesha).copy(name = "Ayesha K."))
    private val newsRepository = TestNewsRepository()

    private fun createViewModel() = PublishViewModel(authRepository, profileRepository, newsRepository)

    private fun PublishViewModel.fillIn(headline: String = "Rain expected", description: String = "Bring an umbrella.") {
        onIntent(PublishIntent.HeadlineChanged(headline))
        onIntent(PublishIntent.DescriptionChanged(description))
    }

    @Test
    fun emptyForm_isNotPublished() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(PublishIntent.Publish)

        assertEquals(FieldError.Blank, viewModel.state.value.headlineError)
        assertEquals(FieldError.Blank, viewModel.state.value.descriptionError)
        assertTrue(newsRepository.publications.isEmpty())
    }

    @Test
    fun tooLongHeadline_isRejected() = runTest {
        val viewModel = createViewModel()
        viewModel.fillIn(headline = "x".repeat(Validation.MAX_HEADLINE_LENGTH + 1))

        viewModel.onIntent(PublishIntent.Publish)

        assertEquals(FieldError.TooLong, viewModel.state.value.headlineError)
        assertNull(viewModel.state.value.descriptionError)
    }

    @Test
    fun publishing_usesTheProfileNameAsByline_andClearsTheForm() = runTest {
        val viewModel = createViewModel()
        viewModel.fillIn(headline = "  Rain expected ", description = " Bring an umbrella. ")

        viewModel.effects.test {
            viewModel.onIntent(PublishIntent.Publish)
            assertEquals(PublishEffect.Published, awaitItem())
        }

        assertEquals(
            listOf(Publication("Rain expected", "Bring an umbrella.", ayesha.copy(name = "Ayesha K."))),
            newsRepository.publications,
        )
        assertEquals(PublishState(), viewModel.state.value)
    }

    @Test
    fun whilePublishing_anotherPublishIsIgnored() = runTest {
        newsRepository.gate = CompletableDeferred()
        val viewModel = createViewModel()
        viewModel.fillIn()

        viewModel.onIntent(PublishIntent.Publish)
        viewModel.onIntent(PublishIntent.Publish)

        assertTrue(viewModel.state.value.isPublishing)
        assertEquals(1, newsRepository.publications.size)
    }

    @Test
    fun failure_keepsWhatTheUserTyped() = runTest {
        newsRepository.failure = BackendError.Network
        val viewModel = createViewModel()
        viewModel.fillIn()

        viewModel.effects.test {
            viewModel.onIntent(PublishIntent.Publish)
            assertEquals(PublishEffect.ShowError(BackendError.Network), awaitItem())
        }

        assertEquals(PublishState(headline = "Rain expected", description = "Bring an umbrella."), viewModel.state.value)
    }
}
