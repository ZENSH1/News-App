package com.xs.newsapp.feature.editprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xs.newsapp.R
import com.xs.newsapp.core.mvi.ObserveEffects
import com.xs.newsapp.core.ui.components.FormTextField
import com.xs.newsapp.core.ui.components.LoadingButton
import com.xs.newsapp.core.ui.components.LoadingContent
import com.xs.newsapp.core.ui.components.message
import com.xs.newsapp.core.ui.components.messageRes
import com.xs.newsapp.core.ui.theme.NewsTheme
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            EditProfileEffect.NavigateBack -> onNavigateBack()
            is EditProfileEffect.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(resources.getString(effect.error.messageRes()))
            }
        }
    }

    EditProfileContent(state = state, onIntent = viewModel::onIntent, snackbarHostState = snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    state: EditProfileState,
    onIntent: (EditProfileIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = { onIntent(EditProfileIntent.BackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.navigate_back))
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingContent(Modifier.padding(innerPadding))
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = state.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FormTextField(
                value = state.name,
                onValueChange = { onIntent(EditProfileIntent.NameChanged(it)) },
                label = stringResource(R.string.field_name),
                leadingIcon = Icons.Filled.Person,
                error = state.nameError?.message(),
                enabled = !state.isSaving,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
            )
            FormTextField(
                value = state.age,
                onValueChange = { onIntent(EditProfileIntent.AgeChanged(it)) },
                label = stringResource(R.string.field_age),
                leadingIcon = Icons.Filled.Face,
                error = state.ageError?.message(),
                enabled = !state.isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            )
            LoadingButton(
                text = stringResource(R.string.save),
                isLoading = state.isSaving,
                onClick = {
                    focusManager.clearFocus()
                    onIntent(EditProfileIntent.Save)
                },
            )
        }
    }
}

@Preview
@Composable
private fun EditProfileContentPreview() {
    NewsTheme {
        EditProfileContent(
            state = EditProfileState(isLoading = false, email = "demo@newsapp.test", name = "Demo Reporter", age = "27"),
            onIntent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
