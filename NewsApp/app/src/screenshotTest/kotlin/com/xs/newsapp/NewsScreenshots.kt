package com.xs.newsapp

import android.content.res.Configuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.Article
import com.xs.newsapp.data.model.UserProfile
import com.xs.newsapp.feature.editprofile.EditProfileContent
import com.xs.newsapp.feature.editprofile.EditProfileState
import com.xs.newsapp.feature.feed.FeedContent
import com.xs.newsapp.feature.feed.FeedState
import com.xs.newsapp.feature.login.LoginContent
import com.xs.newsapp.feature.login.LoginState
import com.xs.newsapp.feature.profile.ProfileContent
import com.xs.newsapp.feature.profile.ProfileState
import com.xs.newsapp.feature.publish.PublishContent
import com.xs.newsapp.feature.publish.PublishState
import com.xs.newsapp.feature.signup.SignUpContent
import com.xs.newsapp.feature.signup.SignUpErrors
import com.xs.newsapp.feature.signup.SignUpState
import com.xs.newsapp.feature.splash.SplashScreen
import com.xs.newsapp.navigation.FeedRoute
import com.xs.newsapp.navigation.MainScaffold
import com.xs.newsapp.navigation.ProfileRoute
import com.xs.newsapp.navigation.PublishRoute
import java.util.Locale
import java.util.TimeZone

// Screenshot tests. Each @PreviewTest is rendered on the JVM and compared with its reference image
// in src/screenshotTestMockDebug/reference (the README shows those images).
//   ./gradlew :app:updateMockDebugScreenshotTest     record new reference images
//   ./gradlew :app:validateMockDebugScreenshotTest   compare against them
// In MVI a screen is a function of its state, so each screenshot is just a state.

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun SplashScreenshot() = Screenshot {
    SplashScreen(onFinished = {})
}

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun LoginScreenshot() = Screenshot {
    LoginContent(
        state = LoginState(email = "demo@newsapp.test", password = "password"),
        onIntent = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun SignUpWithErrorsScreenshot() = Screenshot {
    SignUpContent(
        state = SignUpState(
            name = "Sana Malik",
            email = "sana@newsapp",
            password = "secret1",
            confirmPassword = "secret",
            errors = SignUpErrors(email = FieldError.InvalidEmail, confirmPassword = FieldError.PasswordMismatch),
        ),
        onIntent = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}

@PreviewTest
@Preview(name = "light", showSystemUi = true, device = Devices.PIXEL_7)
@Preview(
    name = "dark",
    showSystemUi = true,
    device = Devices.PIXEL_7,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun FeedScreenshot() = Screenshot {
    MainScaffold(selectedTab = FeedRoute, onTabSelected = {}) {
        FeedContent(state = FeedState(isLoading = false, articles = Articles), onIntent = {})
    }
}

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PublishScreenshot() = Screenshot {
    MainScaffold(selectedTab = PublishRoute, onTabSelected = {}) {
        PublishContent(
            state = PublishState(
                headline = "Night market opens on the riverside",
                description = "Forty stalls will open every Friday from 6 pm, starting next week.",
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun ProfileScreenshot() = Screenshot {
    MainScaffold(selectedTab = ProfileRoute, onTabSelected = {}) {
        ProfileContent(state = OwnProfile, onIntent = {}, snackbarHostState = remember { SnackbarHostState() })
    }
}

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun DeleteStoryDialogScreenshot() = Screenshot {
    MainScaffold(selectedTab = ProfileRoute, onTabSelected = {}) {
        ProfileContent(
            state = OwnProfile.copy(articlePendingDeletion = OwnProfile.articles.first()),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@PreviewTest
@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun EditProfileScreenshot() = Screenshot {
    MainScaffold(selectedTab = ProfileRoute, onTabSelected = {}) {
        EditProfileContent(
            state = EditProfileState(isLoading = false, email = Demo.email, name = Demo.name, age = "27"),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@Composable
private fun Screenshot(content: @Composable () -> Unit) {
    // Reference images must not depend on the time zone or language of the machine rendering them.
    remember {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)
    }
    NewsTheme(content = content)
}

// 2026-06-01 09:30 UTC
private const val NOW = 1_780_306_200_000L
private const val MINUTE = 60_000L
private const val HOUR = 60 * MINUTE

private val Demo = UserProfile("demo", "Demo Reporter", "demo@newsapp.test", UserProfile.DEFAULT_ROLE, age = 27)

private val Articles = listOf(
    Article(
        id = "a1",
        headline = "City council approves new cycling lanes",
        description = "Twelve kilometres of protected lanes will connect the university to the old town by next spring.",
        authorId = "ayesha",
        authorName = "Ayesha Khan",
        publishedAtEpochMillis = NOW - 20 * MINUTE,
    ),
    Article(
        id = "a2",
        headline = "Local team reaches the national final",
        description = "A late goal sealed a 2-1 win in front of a sold-out crowd on Saturday night.",
        authorId = Demo.userId,
        authorName = Demo.name,
        publishedAtEpochMillis = NOW - 2 * HOUR,
    ),
    Article(
        id = "a3",
        headline = "Heatwave expected to break this weekend",
        description = "Forecasters predict cooler winds and scattered showers from Sunday afternoon.",
        authorId = "bilal",
        authorName = "Bilal Ahmed",
        publishedAtEpochMillis = NOW - 5 * HOUR,
    ),
    Article(
        id = "a4",
        headline = "Library opens a 24-hour study hall",
        description = "Students can book seats online; the hall has quiet zones and free Wi-Fi.",
        authorId = Demo.userId,
        authorName = Demo.name,
        publishedAtEpochMillis = NOW - 26 * HOUR,
    ),
)

private val OwnProfile = ProfileState(
    isLoading = false,
    profile = Demo,
    articles = Articles.filter { it.authorId == Demo.userId },
)
