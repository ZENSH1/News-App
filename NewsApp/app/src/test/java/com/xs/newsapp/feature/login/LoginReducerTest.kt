package com.xs.newsapp.feature.login

import com.xs.newsapp.core.validation.FieldError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pure function: no coroutines or fakes needed. */
class LoginReducerTest {

    @Test
    fun editingAField_clearsOnlyThatFieldsError() {
        val state = LoginState(emailError = FieldError.InvalidEmail, passwordError = FieldError.Blank)

        val next = LoginReducer.reduce(state, LoginMutation.EmailUpdated("ayesha@newsapp.test"))

        assertEquals(LoginState(email = "ayesha@newsapp.test", passwordError = FieldError.Blank), next)
    }

    @Test
    fun submission_startsAndFinishes() {
        val submitting = LoginReducer.reduce(LoginState(), LoginMutation.SubmissionStarted)
        assertTrue(submitting.isSubmitting)

        assertFalse(LoginReducer.reduce(submitting, LoginMutation.SubmissionFinished).isSubmitting)
    }

    @Test
    fun passwordVisibility_toggles() {
        val visible = LoginReducer.reduce(LoginState(), LoginMutation.PasswordVisibilityToggled)
        assertTrue(visible.isPasswordVisible)

        assertFalse(LoginReducer.reduce(visible, LoginMutation.PasswordVisibilityToggled).isPasswordVisible)
    }
}
