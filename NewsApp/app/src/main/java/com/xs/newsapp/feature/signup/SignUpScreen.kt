package com.xs.newsapp.feature.signup

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            SignUpEffect.NavigateBack -> onNavigateBack()
            is SignUpEffect.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(resources.getString(effect.error.messageRes()))
            }
        }
    }

    SignUpContent(state = state, onIntent = viewModel::onIntent, snackbarHostState = snackbarHostState)
}

@Composable
fun SignUpContent(
    state: SignUpState,
    onIntent: (SignUpIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val enabled = !state.isSubmitting
    AuthScaffold(
        title = stringResource(R.string.sign_up_title),
        subtitle = stringResource(R.string.sign_up_subtitle),
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) {
        FormTextField(
            value = state.name,
            onValueChange = { onIntent(SignUpIntent.NameChanged(it)) },
            label = stringResource(R.string.field_name),
            leadingIcon = Icons.Filled.Person,
            error = state.errors.name?.message(),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )
        FormTextField(
            value = state.email,
            onValueChange = { onIntent(SignUpIntent.EmailChanged(it)) },
            label = stringResource(R.string.field_email),
            leadingIcon = Icons.Filled.Email,
            error = state.errors.email?.message(),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        )
        PasswordTextField(
            value = state.password,
            onValueChange = { onIntent(SignUpIntent.PasswordChanged(it)) },
            label = stringResource(R.string.field_password),
            isVisible = state.isPasswordVisible,
            onToggleVisibility = { onIntent(SignUpIntent.TogglePasswordVisibility) },
            error = state.errors.password?.message(),
            enabled = enabled,
            imeAction = ImeAction.Next,
        )
        PasswordTextField(
            value = state.confirmPassword,
            onValueChange = { onIntent(SignUpIntent.ConfirmPasswordChanged(it)) },
            label = stringResource(R.string.field_confirm_password),
            isVisible = state.isPasswordVisible,
            onToggleVisibility = { onIntent(SignUpIntent.TogglePasswordVisibility) },
            error = state.errors.confirmPassword?.message(),
            enabled = enabled,
            onImeAction = {
                focusManager.clearFocus()
                onIntent(SignUpIntent.Submit)
            },
        )
        LoadingButton(
            text = stringResource(R.string.sign_up_button),
            isLoading = state.isSubmitting,
            onClick = {
                focusManager.clearFocus()
                onIntent(SignUpIntent.Submit)
            },
        )
        TextButton(
            onClick = { onIntent(SignUpIntent.LogInClicked) },
            enabled = enabled,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.have_account_log_in))
        }
    }
}
