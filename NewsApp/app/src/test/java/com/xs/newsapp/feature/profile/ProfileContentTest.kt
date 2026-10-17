package com.xs.newsapp.feature.profile

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xs.newsapp.R
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.testing.article
import com.xs.newsapp.testing.ayesha
import com.xs.newsapp.testing.profileOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** The dialog is state: shown while [ProfileState.articlePendingDeletion] is set. */
@RunWith(AndroidJUnit4::class)
class ProfileContentTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val intents = mutableListOf<ProfileIntent>()
    private val story = article("a1", ayesha)
    private val loaded = ProfileState(isLoading = false, profile = profileOf(ayesha), articles = listOf(story))

    private fun string(@StringRes id: Int): String = composeRule.activity.getString(id)

    private fun setContent(state: ProfileState) {
        composeRule.setContent {
            NewsTheme {
                ProfileContent(state = state, onIntent = { intents += it }, snackbarHostState = remember { SnackbarHostState() })
            }
        }
    }

    @Test
    fun noPendingDeletion_noDialog() {
        setContent(loaded)

        composeRule.onNodeWithText(string(R.string.delete_article_title)).assertDoesNotExist()
    }

    @Test
    fun pendingDeletion_showsTheDialog_andConfirmingSendsTheIntent() {
        setContent(loaded.copy(articlePendingDeletion = story))
        composeRule.onNodeWithText(string(R.string.delete_article_title)).assertIsDisplayed()

        composeRule.onNode(hasText(string(R.string.delete)) and hasAnyAncestor(isDialog())).performClick()

        assertEquals(listOf(ProfileIntent.DeleteConfirmed), intents)
    }
}
