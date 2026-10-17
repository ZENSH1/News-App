package com.xs.newsapp.core.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidationTest {

    @Test
    fun email() {
        assertEquals(FieldError.Blank, Validation.email("  "))
        assertEquals(FieldError.InvalidEmail, Validation.email("ayesha@"))
        assertEquals(FieldError.InvalidEmail, Validation.email("ayesha newsapp.test"))
        assertNull(Validation.email(" ayesha@newsapp.test "))
    }

    @Test
    fun newPassword_needsAMinimumLength() {
        assertEquals(FieldError.Blank, Validation.newPassword(""))
        assertEquals(FieldError.PasswordTooShort, Validation.newPassword("x".repeat(Validation.MIN_PASSWORD_LENGTH - 1)))
        assertNull(Validation.newPassword("x".repeat(Validation.MIN_PASSWORD_LENGTH)))
    }

    @Test
    fun passwordConfirmation_mustMatch() {
        assertEquals(FieldError.Blank, Validation.passwordConfirmation("secret1", ""))
        assertEquals(FieldError.PasswordMismatch, Validation.passwordConfirmation("secret1", "secret2"))
        assertNull(Validation.passwordConfirmation("secret1", "secret1"))
    }

    @Test
    fun maxLength_ignoresSurroundingWhitespace() {
        assertEquals(FieldError.Blank, Validation.maxLength("   ", 3))
        assertNull(Validation.maxLength("  abc  ", 3))
        assertEquals(FieldError.TooLong, Validation.maxLength("abcd", 3))
    }

    @Test
    fun optionalAge_isBlankOrAWholeNumberInRange() {
        assertNull(Validation.optionalAge(""))
        assertNull(Validation.optionalAge("${Validation.MIN_AGE}"))
        assertNull(Validation.optionalAge("${Validation.MAX_AGE}"))
        assertEquals(FieldError.InvalidAge, Validation.optionalAge("${Validation.MIN_AGE - 1}"))
        assertEquals(FieldError.InvalidAge, Validation.optionalAge("${Validation.MAX_AGE + 1}"))
        assertEquals(FieldError.InvalidAge, Validation.optionalAge("abc"))
    }
}
