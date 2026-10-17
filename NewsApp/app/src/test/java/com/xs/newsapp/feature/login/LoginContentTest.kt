package com.xs.newsapp.feature.login

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xs.newsapp.R
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.core.validation.FieldError
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Feeds the View a [LoginState] and checks the [LoginIntent]s it sends. Robolectric. */
@RunWith(AndroidJUnit4::class)
class LoginContentTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val intents = mutableListOf<LoginIntent>()

    private fun string(@StringRes id: Int): String = composeRule.activity.getString(id)

    private fun setContent(state: LoginState) {
        composeRule.setContent {
            NewsTheme {
                LoginContent(state = state, onIntent = { intents += it }, snackbarHostState = remember { SnackbarHostState() })
            }
        }
    }

    @Test
    fun typingAnEmail_sendsEmailChanged() {
        setContent(LoginState())

        composeRule.onNodeWithText(string(R.string.field_email)).performTextInput("ayesha@newsapp.test")

        assertEquals(listOf(LoginIntent.EmailChanged("ayesha@newsapp.test")), intents)
    }

    @Test
    fun clickingLogIn_sendsSubmit() {
        setContent(LoginState(email = "ayesha@newsapp.test", password = "password"))

        composeRule.onNodeWithText(string(R.string.login_button)).performScrollTo().performClick()

        assertEquals(listOf(LoginIntent.Submit), intents)
    }

    @Test
    fun fieldError_isShownUnderTheField() {
        setContent(LoginState(email = "ayesha@", emailError = FieldError.InvalidEmail))

        composeRule.onNodeWithText(string(R.string.error_email_invalid), useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun whileSubmitting_theButtonShowsProgressInsteadOfItsLabel() {
        setContent(LoginState(email = "ayesha@newsapp.test", password = "password", isSubmitting = true))

        composeRule.onNodeWithText(string(R.string.login_button)).assertDoesNotExist()
    }
}
