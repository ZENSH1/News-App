package com.xs.newsapp.feature.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xs.newsapp.R
import com.xs.newsapp.core.mvi.ObserveEffects
import com.xs.newsapp.core.ui.components.AuthScaffold
import com.xs.newsapp.core.ui.components.FormTextField
import com.xs.newsapp.core.ui.components.LoadingButton
import com.xs.newsapp.core.ui.components.PasswordTextField
import com.xs.newsapp.core.ui.components.message
import com.xs.newsapp.core.ui.components.messageRes
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.core.validation.FieldError
import kotlinx.coroutines.launch

/**
 * Connects the login View to its store: renders [LoginViewModel.state], turns effects into
 * navigation and snackbars, and sends every user action back as a [LoginIntent].
 */
@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            LoginEffect.NavigateToSignUp -> onNavigateToSignUp()
            is LoginEffect.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(resources.getString(effect.error.messageRes()))
            }
        }
    }

    LoginContent(state = state, onIntent = viewModel::onIntent, snackbarHostState = snackbarHostState)
}

/** The View: a pure function of [LoginState] that reports what the user does as [LoginIntent]s. */
@Composable
fun LoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    AuthScaffold(
        title = stringResource(R.string.login_title),
        subtitle = stringResource(R.string.login_subtitle),
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) {
        FormTextField(
            value = state.email,
            onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
            label = stringResource(R.string.field_email),
            leadingIcon = Icons.Filled.Email,
            error = state.emailError?.message(),
            enabled = !state.isSubmitting,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        )
        PasswordTextField(
            value = state.password,
            onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
            label = stringResource(R.string.field_password),
            isVisible = state.isPasswordVisible,
            onToggleVisibility = { onIntent(LoginIntent.TogglePasswordVisibility) },
            error = state.passwordError?.message(),
            enabled = !state.isSubmitting,
            onImeAction = {
                focusManager.clearFocus()
                onIntent(LoginIntent.Submit)
            },
        )
        LoadingButton(
            text = stringResource(R.string.login_button),
            isLoading = state.isSubmitting,
            onClick = {
                focusManager.clearFocus()
                onIntent(LoginIntent.Submit)
            },
        )
        TextButton(
            onClick = { onIntent(LoginIntent.CreateAccountClicked) },
            enabled = !state.isSubmitting,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.no_account_sign_up))
        }
        val hint = stringResource(R.string.login_hint)
        if (hint.isNotEmpty()) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun LoginContentPreview() {
    NewsTheme {
        LoginContent(
            state = LoginState(email = "not-an-email", emailError = FieldError.InvalidEmail),
            onIntent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
